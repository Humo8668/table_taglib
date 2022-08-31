<%@ taglib uri="uz.app.jsp.Table.Tags" prefix="t" %>  
<html>
<body>
    <div style="width: 600px; margin: auto;">
        <t:table name="first_table" dataJson="{rows: [{'asd': 'qwe', 'num': 1}, {'asd': 'zxc', 'num': 2}]}">
            <t:column name="asd" orderValue="1" label="Some Asd" show="true" />
            <t:column name="num" orderValue="2" label="Some number" show="true" />
            <t:column name="empty" orderValue="3" label="Empty field" show="true" />
        </t:table>
    </div>
    
    <div style="width: 600px; margin: auto;">
        <t:table name="second_table" caption="Second table"
        dataJson="{rows: [{'name': 'Steve', 'age': 20.2, 'is_alive': 'No', 'account_sum': 145000000.5196}, {'name': 'Lucas', 'age': 22, 'is_alive': 'Yes', 'account_sum': 022.074542}]}">
            <t:column name="name" orderValue="1" label="Employee name" show="true" />
            <t:column name="age" orderValue="2" type="number" label="Age" format=" |.|0" show="true" />
            <t:column name="account_sum" orderValue="4" type="number" label="Amount of money in account" show="true" />
            <t:column name="is_alive" orderValue="3" label="Is alive" show="true" />
        </t:table>
    </div>

    <div style="width: 600px; margin: auto;">
        <t:table name="employee_table" caption="Employees" dataStoreClass="uz.app.jsp.Table.TestModule.EmployeeDataStore" numeration="true">
            <t:column name="id" orderValue="1" label="Employee ID" show="true" />
            <t:column name="name" orderValue="2" label="Name" show="true" />
            <t:column name="depId" orderValue="4" type="number" label="Department ID" show="true" />
            <t:column name="postId" orderValue="3" type="number" label="Post ID" show="true" />
            <t:column name="salary" orderValue="5" type="number" label="Salary" show="true" />
        </t:table>
    </div>
</body>
</html>
