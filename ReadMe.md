# ControlsManager Model 4.2
DON'T RELY ON THIS DOCUMENTATION IF YOU CAN JUST ASK ME! PLEASEEEEE

## ⚠️ Files in main directory are old, go to testingEnvironment

## Adding this to your project
To add this to a project, simply take the files and plop them in somewhere, and then update the package.____ in each file.

## Terminology
A **Control** is a condition tied to a controller component that has to resolve for something to happen.
A Controller **Component** is a button/trigger/bumper/joystick on a Controller.
A **Controller** is a physical input interface that has a collection of buttons or sticks I guess.
An **XboxController** is WPILib' controller class.

## Values
**Binary:** 0.0 or 1.0 (False or True)
**Threshold:** -1.0 <-> 1.0 (Not held <-> Held) (For the only threshold components on a controller, the Triggers)
**Joystick:** x: -1.0 <-> 1.0 (Left <-> Right) y: -1.0 <-> 1.0 (Up <-> Down)

## Conditions
Conditions are stored like this:
`"<TYPE>:<VALUE>|<TYPE>:<VALUE>&..."` (Seperate conditions with | and &. | = OR, & = AND)
### Binary Conditions
* `ACTIVE` - Resolves if value is 1.0
* `INACTIVE` - Resolves if value is 0.0
### Threshold Conditions
* `LESS_THAN:x` - Resolves if value is less than x
* `LESS_THAN_OR_EQUAL_TO:x` - Resolves if value is less than or equal to x
* `EQUAL_TO:x` - Resolves if value is equal to x
* `GREATER_THAN_OR_EQUAL_TO:x` - Resolves if value is greater than or equal x
* `GREATER_THAN:x` - Resolves if value is greater than x
* `RANGE:x,y` - Resolves if value is within x and y (X is inclusive I think) (Actually it might be Y)
### Joystick Conditions
"Why it lowercase?" - me, february 23rd, 2026
* `north:x` - Resolves if the joystick is going north, x away from the center
* `northeast:x` - Resolve if the joystick is going northeast, x away from the center
* `east:x` - Resolve if the joystick is going east, x away from the center, you catching my rift yet?
* `southeast:x` - Resolve if the joystick is going southeast, x away from the center
* `south:x` - Resolve if the joystick is going south, x away from the center
* `southwest:x` - Resolve if the joystick is going southwest, x away from the center, surely you must be recognizing a pattern
* `west:x` - Resolve if the joystick is going west, x away from the center
* `northwest:x` - Resolve if the joystick is going northwest, x away from the center\
**(⚠️ Joysticks don't always go back to the center, and nor do your fingers! Make sure to set thresholds of about 0.1 - 0.5 from the center)**\
Typical error from center: 0.000005, enough to be considered "north" if no threshold specified

### Binding Functions
Bound Functions have the following properties:
* Name - The name of the control they are bound to
* Once - Weither not to execute only once when the condition becomes true, or to execute each time it's checked as true
* OnInactive - Weither not to execute when the control's condition is false instead of true

### Loggers
**ControlsLogger:** Logs whenever a control's resolve state changes.
**InputLogger:** Logs whenever a controller component's state changes.
**ErrorLogger:** Tells you if anything goes wrong.

## Running ControlsManager
### Input
There are two modes of input detecting: GLFW, and WPI.
WPI Mode uses the XboxController class, managed by WPILib.
GLFW Mode uses GLFW to detect joysticks (Controllers)

### Input Interface
In Main.java, there are 2 variables to determine the selection mode. (BOTH OF THESE ARE NOT SUPPORTED IN WPI MODE)

name-to-select
start-to-select

Name-to-select auto-selects based on name. This is good if you are only using one controller. Things it looks for: "controller", "logitech"
Start-to-select allows controllers to get selected if they press the start button.

## Components
```
Index: The index of the component in Controller's component list
ID: The name of the component in it's enum thingy
WPI Mode: If the control is supported in WPILib's XboxController class
GLFW Mode: If the control is recognized by GLFW

Index  ID   Type     Name                  WPI Mode   GLFW Mode  Elaboration
0:     A    Binary   "Button A"            ✓          ✓          A Button
1:     B    Binary   "Button B"            ✓          ✓          B Button
2:     X    Binary   "Button X"            ✓          ✓          X Button
3:     Y    Binary   "Button Y"            ✓          ✓          Y Button
4:     DL   Binary   "DPad Left"           ✓          ✓          Left DPad button
5:     DR   Binary   "DPad Right"          ✓          ✓          Right DPad button
6:     DU   Binary   "DPad Up"             ✓          ✓          Left DPad Up
7:     DD   Binary   "DPad Down"           ✓          ✓          Left DPad Down
8:     JA   Binary   "Joystick A Push"     ?          x           Pushing the Left Joystick
9:     JB   Binary   "Joystick B Push"     ?          x           Pushing the Right Joystick
10:    SA   Binary   "Special Button A"    ✓          ✓          Back Button (Left small button in the middle of the controller)
11:    SB   Binary   "Special Button B"    ✓          ✓          Start Button (Right small button in the middle of the controller)
--:    (SC) Binary   "Special Button C"    x           x          Mode Switch (Lower left small button in the middle of the controller)
12:    BB   Binary   "Big Button"          x           x          Mode Switch (Lower left small button in the middle of the controller) - (i) Xbox Gamebar overrides this button input 
13:    LB   Binary   "Left Bumper"         ✓          ✓          Left Bumper
14:    RB   Binary   "Right Bumper"        ✓          ✓          Right Bumper
15:    LT   Theshold "Left Trigger"        ✓          ✓          Left Trigger   
16:    RT   Theshold "Right Trigger"       ✓          ✓          Right Trigger
17:    AX   Threshold "Joystick A - X"     ✓          ✓          Left Joystick's X axis
18:    AY   Threshold "Joystick A - Y"     ✓          ✓          Left Joystick's Y axis
19:    BX   Threshold "Joystick B - X"     ✓          ✓          Right Joystick's X axis
20:    BY   Threshold "Joystick B - Y"     ✓          ✓          Right Joystick's Y axis
21:    A    Joystick  "Joystick A"         ✓          ✓          Left Joystick
22:    B    Joystick  "Joystick B"         ✓          ✓          Right Joystick
```
