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
        <t:table name="third_table" caption="third table">
            <t:column name="name" orderValue="1" label="Employee name" show="true" />
            <t:column name="age" orderValue="2" type="number" label="Age" format=" |.|0" show="true" />
            <t:column name="account_sum" orderValue="4" type="number" label="Amount of money in account" show="true" />
            <t:column name="is_alive" orderValue="3" label="Is alive" show="true" />
        </t:table>
    </div>
</body>
</html>
