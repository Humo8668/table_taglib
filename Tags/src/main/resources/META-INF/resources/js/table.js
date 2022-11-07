
function isDef(obj) {
  return (typeof obj !== 'undefined');
}

function isNull(obj) {
  return (typeof obj !== 'undefined' && obj == null);
}

function isDOMElement(obj) {
  try {
    //Using W3 DOM2 (works for FF, Opera and Chrome)
    return obj instanceof HTMLElement;
  }
  catch(e){
    //Browsers not supporting W3 DOM2 don't have HTMLElement and
    //an exception is thrown and we end up here. Testing some
    //properties that all elements have (works on IE7)
    return (typeof obj==="object") &&
      (obj.nodeType===1) && (typeof obj.style === "object") &&
      (typeof obj.ownerDocument ==="object");
  }
}

function isString(obj) {
  return (typeof obj === 'string');
} 

function isEmptyString(obj) {
  return (typeof obj === 'string' &&  (obj == null || obj.length == 0));
}

function getTableManager(){
  if(typeof document.TableManager !== 'undefined') {
    return document.TableManager;
  } else {
    document.TableManager = {
      tables: {},
      getTable: function(tableName) {
        if(isString(tableName) && !isEmptyString(tableName))
          return this.tables[tableName];
        return null;
      },
      registerTable: function(tableName, tableObject) {
        if(!isString(tableName) || isEmptyString(tableName)) {
          return;
        }
        this.tables[tableName] = tableObject;
      },
      getTablesArray: function () {
        var tablesArray = [];
        for(var tbl in this.tables) {
          tablesArray.push(this.tables[tbl]);
        }
        return tablesArray;
      }
    };
    return document.TableManager;
  }
}

var ORDERS = {
  DEFAULT: 0,
  ASC: 1,
  DESC: -1
};

function getNextOrdering(currOrdering) {

  if(currOrdering == ORDERS.DEFAULT || typeof currOrdering === 'undefined') {
    return ORDERS.ASC;
  } else if (currOrdering == ORDERS.ASC) {
    return ORDERS.DESC;
  } else if (currOrdering == ORDERS.DESC) {
    return ORDERS.ASC;
  } else { // keeping code complainable 
    return ORDERS.ASC;
  }

  /*if(currOrdering == ORDERS.DEFAULT || typeof currOrdering === 'undefined') {
    return ORDERS.ASC;
  } else if (currOrdering == ORDERS.ASC) {
    return ORDERS.DESC;
  } else if (currOrdering == ORDERS.DESC) {
    return ORDERS.DEFAULT;
  } else { // keeping code complainable 
    return ORDERS.DEFAULT;
  }*/
}

function Sort(objArray, comparatorFunc) {
  for(var i = 0; i < objArray.length; i++) {
    for(var j = 0; j < objArray.length-i-1; j++) {
      if(comparatorFunc(objArray[j], objArray[j+1]) > 0) {
        var obj = objArray[j];
        objArray[j] = objArray[j+1];
        objArray[j+1] = obj;
      }
    }
  }
}

function equalsIgnoreCase(str1, str2) {
  if(typeof str1 !== 'string' || typeof str2 !== 'string') {
    return false;
  }

  return (new String(str1)).trim().toLowerCase() == (new String(str2)).trim().toLowerCase();
}



function Table(tableName) {
  const INPUT_ROWS_IN_PAGE = "rows_in_page";
  const INPUT_PAGE_NUMBER = "active_page_number";
  this.tableName = tableName;
  this.tableDOM = null;
  this.tableBodyDOM = null;
  this.tableHeadDOM = null;
  this.rowsDOMCollection = null;
  this.columns = {};
  this.tableFormDOM = null;
  this.tableFormInputs = null;
  this.selectedRowDOM = null;
  this.ordering = {};
  getTableManager().registerTable(tableName, this);

  this.getTableDOM = function() {
    return this.tableDOM;
  }

  this.getTableForm = function() {
    return this.tableForm;
  }

  this.tableSubmit = function() {
    var tableParams = {};
    tableParams[INPUT_PAGE_NUMBER] = this.tableFormInputs[INPUT_PAGE_NUMBER].value;
    tableParams[INPUT_ROWS_IN_PAGE] = this.tableFormInputs[INPUT_ROWS_IN_PAGE].value;

    this.tableFormInputs[this.tableName].value = JSON.stringify(tableParams);
    this.tableFormDOM.submit();
  }

  this.onPageChanged = function(pageNum) {
    this.tableFormInputs[INPUT_PAGE_NUMBER].value = pageNum;
    this.tableSubmit();
  }

  this.onRefresh = function() {
    this.tableSubmit();
  }

  this.onRowSelected = function(rowDOM) {
    if(rowDOM == this.selectedRowDOM)
      return;

    rowDOM.setAttribute("active", true);
    rowDOM.classList.add("active");
    if(this.selectedRowDOM != null) {
      this.selectedRowDOM.removeAttribute("active");
      this.selectedRowDOM.classList.remove("active");
    }
    this.selectedRowDOM = rowDOM;
  }

  this.getSelectedRow = function() {
    return this.selectedRowDOM;
  }

  this.getSelectedRowData = function() {
    if(typeof this.selectedRowDOM === 'undefined' || this.selectedRowDOM == null)
      return {};
    
    var TDs = this.selectedRowDOM.getElementsByTagName("td");
    var row = {};
    for(var i = 0; i < TDs.length; i++) {
      var key = TDs[i].getAttribute("name");
      if(typeof key !== 'undefined' && key != null) 
        row[key] = TDs[i].innerText;
    }
    return row;
  }

  this._order = function(orderObj) {
    var arr = [];
    // for each row
    for (var i = 0; i < this.rowsDOMCollection.length; i++) {
      // for each cell in row
      for (var j = 0; j < this.rowsDOMCollection[i].children.length; j++) {
        var colName = this.rowsDOMCollection[i].children[j].getAttribute('name');
        if(equalsIgnoreCase(colName, orderObj.column)) {
          var value = this.rowsDOMCollection[i].children[j].textContent;
          if(equalsIgnoreCase(this.columns[colName].type, 'number')) {
            value = value.replace(' ', '') - 0;
          }
          arr.push({
            value: value,
            td: this.rowsDOMCollection[i].children[j],
            tr: this.rowsDOMCollection[i]
          });
        }
      }
    }

    Sort(arr, function(left, right) {
      if(left.value > right.value) {
        return 1 * orderObj.order;
      } else if(left.value < right.value) {
        return -1 * orderObj.order;
      } else {
        return 0;
      }
    });

    for(var i = 0; i < arr.length; i++) {
      this.tableBodyDOM.appendChild(arr[i].tr); // Cannot be two same DOM-Nodes in one DOM element. So when existing node re-added, first will be deleted.
    }
  }

  this._refreshOrderIcons = function() {
    var columnHeads = this.tableHeadDOM.getElementsByTagName("th");
    for(var i = 0; i < columnHeads.length; i++) {
      if(typeof columnHeads[i].getElementsByTagName("i")[0] !== 'undefined')
        columnHeads[i].removeChild(columnHeads[i].getElementsByTagName("i")[0]);
      var colName = columnHeads[i].getAttribute("name");
      if(typeof colName === 'undefined')
        continue;
      if(typeof this.ordering[colName] === 'undefined')
        continue;

      var icon = document.createElement("i");
      if(this.ordering[colName].order == ORDERS.ASC)
        icon.setAttribute("class", "fa fa-sort-up");
      else if(this.ordering[colName].order == ORDERS.DESC)
        icon.setAttribute("class", "fa fa-sort-down");
      else
        continue;

      columnHeads[i].appendChild(icon);
    }
  }

  this.changeOrdering = function(column) {
    var orderObj = {};
    if(typeof this.ordering[column] !== 'undefined') {
      orderObj = this.ordering[column];
    } else {
      orderObj.column = column;
    }
    orderObj.order = getNextOrdering(orderObj.order);
    this.ordering[column] = orderObj;
    // *****************
    this._order(orderObj);
    // *****************
    this._refreshOrderIcons();
  }

  this.filter = function (filterObj) {
    console.log(filterObj);
  }

  this._onPageLoad = function() {
    this.tableDOM = document.getElementById(this.tableName);
    this.tableBodyDOM = this.tableDOM.getElementsByTagName("tbody")[0];
    this.tableHeadDOM = this.tableDOM.getElementsByTagName("thead")[0];
    this.rowsDOMCollection = this.tableBodyDOM.getElementsByTagName("tr");
    this.tableFormDOM = document.getElementById("table_"+this.tableName+"_form");
    this.tableFormInputs = this.tableFormDOM.getElementsByTagName("input");
    var columnHeads = this.tableHeadDOM.getElementsByTagName('th');
    for(var i = 0; i < columnHeads.length; i++) {
      var name = columnHeads[i].getAttribute("name");
      var type = columnHeads[i].getAttribute("type");
      var DOM = columnHeads[i];
      this.columns[name] = {name: name, type: type, DOM: DOM};
    }
  }
}

function onPageLoad() {
  var tables = getTableManager().getTablesArray();
  for(var i = 0; i < tables.length; i++) {
    let table = tables[i];
    table._onPageLoad();
  }
}

window.addEventListener("load", onPageLoad);