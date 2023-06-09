# Finance Tracker Mobile in Java 💶
This project showcases a simple finance tracker application made with Java programming language. This app helps you to manage your personal finances. It keeps track on your expense and income. In this app, we implemented Create-Read-Update-Delete (CRUD) concept. Several features that you can experience are:
* *Create transaction*<br/><img src="./assets/create_transaction.gif" alt="create_transaction" style="width:250px;" /><br />You can add a new transaction (income/expense) by clicking the plus button on the bottom right corner. Then, the "add transaction" page will be opened. On this page, you should provide the label, amount, and category of the transaction. The description is an optional part. After all required fields are filled, the transaction will be shown on the list on the homepage.
* *Read transaction (filter by selected date range)*<br /><img src="./assets/read_transaction.gif" alt="read_transaction" style="width:250px;" /><br />All transactions can be viewed on the homepage. The header of the homepage is the dashboard that shows the current balance, total income, and total expenses. In addition, you may view some transactions from a specific range of date through the filter below the dashboard. If the filter icon is clicked, it will open a calendar page. On this page, you can pick the desired start date and end date. After you click save, transactions within the date range will be shown on the transaction list.
* *Update transaction*<br /><img src="./assets/edit_transaction.gif"  alt="edit_transaction" style="width:250px;" /><br />This feature enables you to modify a transaction after it has been created. Fields such as label, amount, description, and category, can be easily modified by clicking on the transaction item from the list. The "Edit Transaction" page will be shown and you may edit the value on each field, then click the button on the bottom to save the updated data.
* *Delete transaction*<br /><img src="./assets/delete_transaction.gif" alt="delete_transaction" style="width:250px;" /><br />Deleting a transaction can be easily done by swiping the transaction item to the right.
* *Set daily expense limit*<br /><img src="./assets/set_expense_limit.gif" alt="set_expense_limit" style="width:250px;" /><img src="./assets/expense_limit_alert.gif" alt="expense_limit_alert" style="width:250px;" /><br />Set your daily expense limit by clicking the orange button in the bottom left corner. The app will give you a warning if you add expenses that exceed the limit.

**View the app demo here: [Click Here](https://angeline-mary-marchella.notion.site/Finance-Tracker-218531c3cec1496e9378fd853b692155?pvs=4)**
## Platforms and Libraries
Since this is an Android application, we used [`Android Studio`](https://developer.android.com/studio?gclid=CjwKCAjwpuajBhBpEiwA_ZtfheMV-FhB4RZOHrGrzi_mPgiUudqZsvKb98tI1N4DSrbsDh1-oJeqNRoC94wQAvD_BwE&gclsrc=aw.ds) as the main platform to integrate the frontend and backend of our application. To create the user interface, `.xml` files are created for each activity (page). Besides using several built-in libraries, [`Material.io`](https://m3.material.io/) library is also implemented to create some components in this app, such as dashboard cards, add buttons, etc. Specifically, the library version that we imported in `build.gradle` file is `com.google.android.material:material:1.4.0-alpha02`. This program  also uses an SQLite database to store the transactions

## How to Run the Application
### Run the application locally
* Clone the repository
  Run the following command to clone this repository
  ```
  git clone https://github.com/angelinemarym/Finance-Tracker-Mobile-Java.git
  ```
* Install [`Android Studio`](https://developer.android.com/studio?gclid=CjwKCAjwpuajBhBpEiwA_ZtfheMV-FhB4RZOHrGrzi_mPgiUudqZsvKb98tI1N4DSrbsDh1-oJeqNRoC94wQAvD_BwE&gclsrc=aw.ds)
  <br />Install [`Android Studio`](https://developer.android.com/studio?gclid=CjwKCAjwpuajBhBpEiwA_ZtfheMV-FhB4RZOHrGrzi_mPgiUudqZsvKb98tI1N4DSrbsDh1-oJeqNRoC94wQAvD_BwE&gclsrc=aw.ds) and do the configuration. Also, do not forget to create an emulator to run the application later.
* Run the app <br />
   Once your device or emulator is ready, click on the`Run` button (a green play button) in the toolbar or select `Run` > `Run 'app'` from the top menu. Android Studio will build the app and deploy it to the connected device or emulator.
### Run the application from your mobile phone
* Install the `.apk` file on your device <br/>
  After the `.apk` file is installed, you can open the app and explore the features.
