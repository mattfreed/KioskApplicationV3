# Brigham and Women’s Hospital

## Kiosk Application User Guide

```
April 2019
Version 1.
```
WPI Computer Science Department
CS 3733 Software Engineering – Professor Wilson Wong
Team Coach: Ethan Schutzman
Team G: Ryan Lapointe – Lead Engineer
Scott Gubrud – Assistant Lead Engineer
Anqi Shen – Assistant Lead Engineer
Lewis Cook – Project Manager
Adam Desveaux – Document Analyst
Matt Freed – Product Owner
Eleanor Foltan – Scrum Master
Elie Hess – Test Engineer
Kyria Nelson – Software Engineer


ii

```
Thank you to Brigham and Women's Faulkner Hospital
and Andrew Shinn for their time and input.
```

iii

```
Document Revisions
```
Date (^) NumberVersion Document Changes
04 / 21 /20 19 0.1 Initial Draft


## iv


- 1 Introduction Contents
   - 1.1 Scope and Purpose
   - 1.2 Process Overview
- 2 Process and Workflow
   - 2.1 Main Menu
      - 2.1.1 View Maps
      - 2.1.2 View current Time
      - 2.1.3 Access Main Functionalities
      - 2.1.4 Direct Login
      - 2.1.6 Timeout Session
      - 2.1.7 Acknowledgement
   - 2.2 Get Directions
   - 2.3 Log in
      - 2.3.1 User Profile Page
   - 2.4 Request Service
      - 2.4.1 Custodian Service
      - 2.4.4 Interpreter Service
      - 2.4.5 IT Service
      - 2.4.7 Religious Service
      - 2.4.8 Security Service
      - 2.4.9 Transportation Service
      - 2.4.10 Food Delivery Service
      - 2.4.11 Emergency
   - 2.4.12 Imagining Booking v
- 2.5 Administrative Functions
   - 2.5.1 Manage Service Requests
- 2.6 Book Room
- 2.7 Fun Corner
   - 2.7.1 Snake


## 1 Introduction

### 1.1 Scope and Purpose

```
This Application for Brigham and Women’s Hospital was developed to assist hospital
employees and administrators with their daily tasks, as well as train students in the WPI CS
3733 Software Engineering class. Agile methodology was used during the development,
where the team focused on the need of the users to prioritize tasks and organize the design
process.
```
### 1.2 Process Overview

```
In the application, all users can access the get directions function for navigating in the
hospital. An employee can book rooms in the flexible workspace on the fourth floor and
make service requests. In addition, an administrator has the power to manage employees,
service requests, edit maps, and change kiosk settings. To create a more relaxing work
environment, a Fun Corner was implemented with a snake game to help people in the
hospital destress.
```

## 2 Process and Workflow

### 2.1 Main Menu

#### 2.1.1 View Maps

```
If the user would like to see all the maps of the hospital, they can approach the kiosk and
select the desired tab on the left-hand side.
```


#### 2.1.2 View current Time

```
The user can also check the current date and time in the upper left corner.
```

#### 2.1.3 Access Main Functionalities

```
To access any main functionalities, the user can either click on the big buttons on the bottom
right corner or the main menu buttons on the top.
```

#### 2.1.4 Direct Login

```
If the user would like to log in first, they can click on the “login” button.
```
```
To log out of their account, click on the “log out” button on top to protect your privacy.
```

2.1.5 Navigating the Application

```
At any point in the application, the user may click on the “home button” to go back to home
page.
```
```
Or click on “back” to go back to the previous page.
```

#### 2.1.6 Timeout Session

```
The application will automatically time out to the home page if no one has accessed it for a
certain amount of time. The time out time period can be set by an administrator in the
Admin page. When time out happens, the application will automatically return to main page
and log the user out.
```
#### 2.1.7 Acknowledgement

```
Information about the team and API’s used in the application can be found in the “About”
and “Credit” page on the bottom right corner.
```

### 2.2 Get Directions

```
After the user has clicked on the get direction button, they can select a start and destination
location. There are multiple ways to search for the start and destination locations.
```
1. The user knows the name of the two locations: simply search for the names of the
    locations in the search bars. Fuzzy search with implemented so that the user can be less
    explicit.
2. The user knows the types of the two locations, but is not certain about the name: click on
    the filter option above and select the desired type. The selection box presents all


```
locations with the designated type.
```
3. The user knows the floor of the two locations, but not the exact locations on the map:
    click on the desired floor tab and search through the selection box for the locations.


4. The user knows the locations on the map and needs to get the directions: go to the floor
    and click on the location on the map to enter the start and destination locations. The
    Toggle on the bottom switches between start location and destination. On the map, start
    location will be marked green, and the destination will be marked red.

```
After the user has selected both start and destination locations, they can click on the get
directions button at the bottom right corner. If either field were not filled, an error message
```

would be displayed.

When on the directions result page, the user will still have access to the main menu bar on
top. On the left side, the tabs not only indicate the floor, but also the steps of the path. In the
middle, the user will see a visual representation of the path on a selected floor. Red signifies
the end of the path on that floor and green represents the start of the path on the floor. An
animated dot will also indicate direction by traveling along the path. The user can mouse


over the start and end locations to see the name of them.

On the left side, the start and destination locations are displayed on top. Textual directions
are displayed in the box underneath. Selecting the step will automatically switch to the
corresponding map.


### 2.3 Log in

```
Users of the application have three tiers of accessibilities. The regular users can get
directions in the hospital. The hospital employees can request services and book rooms. The
administrators can manage employees, service request, change kiosk settings, etc. To access
the functionalities granted to the user, the user needs to first log in with their unique
identification (UID), then the system will check the UID and allow user to perform certain
tasks. There are a few ways to log in.
```
1. The user wants to directly log in to the application before selecting any functionalities:
    They can click on the “Login” button in the main menu as explained in Section 2.1.4.
2. The user wants to make a service request or book a room: Only hospital employees and
    administrators can make service requests or book rooms, so they will be prompted to
    enter their user name and password.


3. The user wants to access administrative functionalities: Only administrators can access
    these functions, so they need to enter the administrative ID and password to enter.

#### 2.3.1 User Profile Page

```
Hospital employees and administrators have profile pages. They can access it after they log
in by clicking on their name on the main menu bar.
```

On the personal profile mainly consists of three parts:

1. Information zone: the user may edit their email and whether they would like to
    receive email notifications.
2. Manage service requests: the user can manage both the requests they made and the
    requests they are assigned to.
       a. For the requests they made, they can view the details and mark it as resolved.


b. For the requests they are assigned to, they can view the details, mark it as
resolved, and get directions to the location.

```
When the user clicks on the “get path” button, they are redirected to the get
directions main page, where the destination is automatically filled in
according to the service request, and the start location is automatically the
default kiosk location. If the user wished to change the start location, they
may do so too.
```

3. View rooms booked by the user: On the calendar on the right side, the user can view
    all the rooms they booked.

```
If the user wishes to see the room schedule on a weekly or monthly view, they can
click on the top buttons to switch views.
```

If the user would like a local copy of the calendar view, they can print the current
calendar view to a pdf file by clicking on the print button.


### 2.4 Request Service

```
After the user has successfully logged in, they may select whichever service they want to
request.
```

#### 2.4.1 Custodian Service

```
When the user wants to request a custodian service, they need to select a location where
custodian service is needed.
```
```
After the location is selected, the user must enter a message briefly describing the type of
issue. If either of the fields were not filled out, clicking on the “confirm” button would
display an error message prompting the user to enter additional information.
When the user has filled out all the fields and clicked “confirm”, the application will record
the service request, which can be viewed by the administrator, and go back to the service
request main screen.
```

2. 4 .2 Delivery Service

```
When the user wants to request a delivery service, they will select a location for pickup
location and drop-off location.
```
```
The user will also need to select the pickup time and drop-off time. Drop-off time must be
after the pickup time, otherwise the application will display an error message when you
```

click “confirm”.

When the user has filled out all the fields and clicked “confirm”, the application will record
the service request, which can be viewed by the administrator, and go back to the service
request main screen.


2. 4 .3 Floral Service

```
The floral service is an API that we produced, which is why it looks different from the other
service requests. However, the user still needs to designate the location of the flower
delivery”.
```

In addition, the user can specify what color flower they want to deliver. They can choose on
the color picker and the floral pattern on the right side will show a previous of the flower.

The user will also fill in the quantity of flowers and delivery time of the floral service before
they confirm the floral service request. When the user has filled out all the fields and clicked
“confirm”, the application will record the service request, which can be viewed by the
administrator, and go back to the service request main screen.


#### 2.4.4 Interpreter Service

```
Similar to the custodian service, the interpreter service first prompts the user to select a
location. Then the user can select the language they need interpreted from a dropdown
menu.
```

#### 2.4.5 IT Service

```
Same as the custodian service request with minor changes.
```

2 .4.6 Maintenance Service

```
Same as the custodian service request with minor changes.
```
#### 2.4.7 Religious Service

```
Similar to the custodian service, the religious service first prompts the user to select a
location. Then the user can enter the religion and the type of service they would like to
```

receive.

Then the user can select a date and time for their religious service. When the user has filled
out all the fields and clicked “confirm”, the application will record the service request,
which can be viewed by the administrator, and go back to the service request main screen.


#### 2.4.8 Security Service

```
Same as the custodian service request with minor changes.
```

#### 2.4.9 Transportation Service

```
Similar to the custodian service, the user needs to enter the location and comment. In
addition, a date and time is required for this transportation service. When the user has filled
out all the fields and clicked “confirm”, the application will record the service request,
which can be viewed by the administrator, and go back to the service request main screen.
```

#### 2.4.10 Food Delivery Service

```
The food delivery is an API provided by the class, and it has its own database for employees
and locations. When click on the food icon on the main service menu, a pop up will show.
Click on the “Staff Menu Order” will bring you to the ordering page. The user can select
menu foods, to add to order, set the delivery location, can confirm.
```

After the order is submitted, the user can then assign the order to an employee on the main
page.

If the user wishes to see the records to of foods and orders, they can click on the “Report”
button and see the statistics.


#### 2.4.11 Emergency

```
The Emergency option is an API provided by team B. This API allows the user to quickly
select an international standard color code for emergency. As the emergency is reported, the
camera of the computer or kiosk will automatically take a picture of the person requesting it
for security reasons.
```

2.4.12 Imagining Booking
The Imaging Booking is an API provided by team A. This API allows the user to schedule
imaging like MRI for patients. The user can fill out all the fields in the main page and
submit the imaging booking to the queue.

```
The queue will organize the list of imaging books by time and whether it is an emergency.
```

## 2.5 Administrative Functions

```
After the administrator has logged in, they have access to additional functionalities unique
to the administrator
```
### 2.5.1 Manage Service Requests

```
The admin can view all the service requests in the database and manage them. On the left-
hand side, there is a list with all service requests. The service requests are marked by Type –
location – message.
```

When a service request is selected, if there are any additional information, they will be
displayed in the information box in the middle top area.

The admin can select an employee from the employees list that was prefiltered by their
capability to fulfill a certain type of service request. Then click the “Assign Employee”
button to assign the selected employee to the service request.


Note that the text “currently assigned to:” will change to the employee you just assigned. If
they are signed up to receive email notification, they would get a message when they are
assigned to a new service request.

The Admin also has the power to mark any service request as resolved when they select the
service and click on the “Resolve Service” button.


2.5.2 Edit Map

```
The admin can visually edit the locations on the hospital maps by clicking on a circle (later
referred to as a node, it represents a physical location in the hospital) on the image on the
floor they want. The blue text box at the bottom left corner provides instructions for first
time users.
```
```
There are five operations of be done with the nodes:
```
1. Adding a node: The admin wants to add a new node that is not on the map currently.
    They can switch the toggle on the bottom right corner to “add a node”, then click on
    the map to add a node at the point they click. A small window will show up with all
    the nodes information fields. The x, and y pixel locations and the floor fields are
    automatically set based on where the admin clicks. The user will need to manually
    fill in the building, node type, a long name, and a short name for the node, then click
    “Add node”.


```
After the node has been added, the admin can then access functions 2-5 normally on
the new node.
```
2. Editing information of a selected node: The admin wants to edit the information
    fields as mentioned in the previous bullet point of the node, such as the type and the
    name. They can click on the “Edit Node” button after they click on the selected
    node, then precede to change the information in the fields. Click “Edit Node” on this
    page will persist the changes to the local database.


3. Edit Edge: If the admin wants to edit or delete a connection between two nodes
    (referred to as an edge), with one side of the edge on the node they have clicked on,
    they can choose the “Edit Edge” option. The selected edge will turn green.

```
To add an edge, simply click on the node they want to connect to, and a line
representing an edge will appear after the click indicating the edge has been
successfully added.
```
```
To delete an edge, click on one of the nodes. The selected node will turn green and
its connected neighboring nodes will turn red. Then the admin can click on the red
```

```
node to select the edge. The edge will disappear indicating that it has been
successfully deleted.
```
4. Delete Node: Click on the “Delete Node” option to delete the selected node. The
    window will ask for confirmation again, where they can “Delete” or “Cancel”. If the
    admin selects delete, the node, along with all of its edges, will be deleted from
    database and disappear from the map.
5. Drag Node: To drag the node to another location on map on the same floor the
    admin will click on the “Drag Node” option, then drag it to the new location. The
    edges of this node will stay in place to show the old location of the node during


```
dragging. Upon the mouse release, the edges will be connected to the node at the
new location again.
```
If the admin wishes to cancel the action during editing, they can click on the “Cancel
Action” on the bottom right corner (above the “add a node” toggle).

All the changes are persisted to the local database. To permanently change the map,
download map locally using the “Download Map” button at bottom right corner (above the
“add a node” toggle), then substitute the edges and node csv files with the new ones.


2.5.3 Edit Employee

```
An admin has access to all the employee information and can manage them:
```
1. Add an employee: the admin can add an employee by filling in their information.
2. Edit the type of services they can fulfill: the admin can click on the services column
    to show a pop of all services. By changing the check boxes, the admin can change the
    employee’s ability to fulfill a certain type of service request. When the employee is
    able to fulfill a service request, their name will appear in the employee assignment list
    as an option. The admin by default can fulfill all service requests.


2.5.4 View Service Request Statistics

```
Statistics of service request, such as time of service needed and types of service, can be
viewed on this page to help the admin make executive decisions.
```
2.5.5 Edit Kiosk settings

```
There are two kiosk settings that the admin can change, they are located on the main
Administrative page at the bottom right corner under the other functionalities:
```
1. Change the timeout time period: the kiosk application will time out after an admin
    determined time (see 2.1.6 for more details on the time out session). The default
    timeout is 30 seconds, and the admin can change to any duration greater than 1
    second.
2. Change the path finding algorithm: multiple path finding algorithms were written for
    this application for educational purposes. The default A* algorithm is the most
    efficient and optimal path, and it is highly recommended. The admin has the power to
    change it to Breath-First Search (BFS), Depth-First Search (DFS), Best-First Search,
    and Dijkstra.


## 2.6 Book Room

```
Employees and admins can book rooms and desks in the Flexible Workspace on the fourth
floor. The main page shows the current availability of the all the bookable space. Red means
occupied, and green means available. To view the availability of room at a given time for
booking, the user can change the time period on the upper right corner.
```
```
To book a space, the user will select the room or desk on the map by clicking inside the
colored area. The area will show selection with a blue border. The calendar on the right side
will show the space’s name and availability in a user-friendly calendar view. The user can
```

double click to add a booking directly on the calendar.

The user can change the name and time of the event by clicking on the booking. They can
also drag the booking on the calendar or change the size of it. Availability map will
automatically update as the user adds a booking when the correct time frame is been
displayed. Conflicting bookings are prevented by the system to ensure no double booking.
Note the desks can only be booked 15 minutes in advance, and bookings beyond that range
will automatically be reset to start at current time.

Like the calendar in the user profile page, this calendar can show week and month views, as
well as print to local copy (see 2.3.1 bullet 3 for more details).

The bookings made by the user will appear in the user’s profile page calendar (see 2.3.1
bullet 3 for more details).


## 2.7 Fun Corner

```
A nonage restricted fun corner was developed to help the user destress. The button is
location on the bottom left of the home screen.
```
### 2.7.1 Snake

```
A snake game was developed using the maps of the hospital and location nodes.
The user can press the arrow keys or the WASD keys to start and play. Press R will restart
the game. The game will be over if the snake runs into the edge or itself. The snake starts on
lower floor 2. Eating all nodes on a floor will let the snake enter the floor above until the
fourth floor is reached and all nodes are eaten.
On the left-hand side, game rules are displayed. The cumulative score will show and update
as the snake eats more nodes. It will also let the user know how many nodes are left on the
current floor. If the game is over, the application will show the final score and “Game Over”
in red.
```

If the user beats all the levels, they beat the game and will be congratulated.

Full documentation with images can be found in the PDF located in this repository 
