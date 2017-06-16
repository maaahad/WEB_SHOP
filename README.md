#Task Specification:

In this project I have developed a web app for online shopping from the view point of both buyer and adminstrator. The app worked like an online shop where a buyer can browse through the available products and save it to his shopping cart to buy instantly or later. On the other hand the app can be used for adminstration purpose, for example: an admin can add or remove an existing product along with impose ban to any user.

#Implementation Platform:
The app was developed on Mac OS X EL Capitan Verion 10:11:4 using Netbeans 8.1 IDE.


#Java software:
A number of Java Software has been used for developing this app. For building component-based user interfaces JavaServer Faces(JSF) along with open source Framework PrimeFaces 4.0 was used. Managed Beans was used to work as model for UI component. For performing business logic Enterprise JavaBean (EJB) was used as a server-side scomponent. EJB contains model that is persisted by JPA and for DBMS mysql was used. GlassFish 4.0 was used as deployment tools.


#Detailed description of the application:
The application start with html page provided the user to login, register or view the available products. To log in the user needs to be registered, otherwise a pop up will be shown for the user to be registered. Once the user is logged in, he/ she can loged out any time. After logged in the user can choose item and add to his cart. The user can come back any time to buy or edit the product in his cart. On the other hand, if the user is an admin, he will be provided with option to add or remove item from the inventory, see the all registered users and impose ban or even commisioned a buyer as admin.


#A brief of Implementation:
To manage view related to a common user, a user bean with the class name UserBean.class has been imlemented which is a client to a EJB named UserEJB.class. Business logic is performed in the UserEJB.class. UserEJB.class persisted entities to database. Fom admin specific function AdminBean.class as managed bean and AdminEJB.class as EJB was used. For both cases the bean was SessionScoped and the EJB were Stateful as we need to keep the state of the current user in the EJB.

For managing data to the database three different kinds of JPA entities was needed for the application.
1. UserEntity.class with datatable name USER_INFO for storing information related to a user
2. OrderEntity.class with datatable name ORDERS for storing all orders for every user
3. GnomeEntity.class with datatable name GNOME_INVENTORY for storing products items
Both USER_INFO and GNOME_INVENTORY have OneToMany relationship with the ORDERS datatable.
