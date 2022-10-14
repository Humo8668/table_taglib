
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


function Table(tableName) {
  const INPUT_ROWS_IN_PAGE = "rows_in_page";
  const INPUT_PAGE_NUMBER = "page_number";
  this.tableName = tableName;
  this.tableDOM = null;
  this.tableFormDOM = null;
  this.tableFormInputs = null;
  getTableManager().registerTable(tableName, this);

  this.getTableDOM = function() {
    return this.tableDOM;
  }

  this.getTableForm = function() {
    return this.tableForm;
  }

  this.tableSubmit = function() {
    this.tableFormInputs[this.tableName].value = 
      this.tableFormInputs[INPUT_PAGE_NUMBER].value +
      ";" +
      this.tableFormInputs[INPUT_ROWS_IN_PAGE].value +
      "";
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
}

function onPageLoad() {
  var tables = getTableManager().getTablesArray();
  for(var i = 0; i < tables.length; i++) {
    let table = tables[i];
    table.tableDOM = document.getElementById(table.tableName);
    table.tableFormDOM = document.getElementById("table_"+table.tableName+"_form");
    table.tableFormInputs = table.tableFormDOM.getElementsByTagName("input");
  }
}

window.addEventListener("load", onPageLoad);