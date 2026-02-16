  # ControlsManager (Model 3)


To add this to a project, make sure to add package lines at the start of the files!


## Methods

`Controls.addBinary(name,component,condition)` - Adds a binary control to the cache\
`Controls.addThreshold(name,component,condition)` - Adds a threshold control to the cache.\
`Controls.addJoystick(name,component,condition)` - Adds a joystick control to the cache.\
`Controls.get(name)` - Fetches a control from the cache.\
`Controls.getJoystickAngle(joystickComponent)` - Gets Joystick A or B's angle.\
`Controls.getJoystickCondition(joystickComponent,condition,controller)` - Gets weither not a joystick condition is true.\
`Controls.bindFunctionToControl(name,onceOnTrue,function)` - Binds a function to a control when it's condition is true.\
`Controls.conditionResolve(name)` - Gets weither not the control's condition is true.\
`Controls.controlsLogger(on)` - Turns on/off the Controls Logger, which gives output whenever a control's condition resolvation changes.\
`Controls.inputLogger(on,suppressJoystickOutput)` - Turns on/off the Input Logger, which gives output whenever a controller component's value changes.\
`Controls.errorLogger(on)` - Turns on/off the Error Logger, which gives output whenever something goes wrong.\
\
`Control.conditionResolve()` - Get's weither not that control's condition is true.\
`Control.bindFunction(function,executeOnceUponResolve)` - Binds a function to the control directly.
`Control.bindedFunction()` - Calls the binded function to the control object.
`Control.setCondition(complexCondition)` - Changes the condition for the control object.
`Control.setControl(control)` - Changes the control to something else.

## Terminology
A **component** is a button, trigger, bumper, or joystick on a controller.\
A **control** is an action you do with a component.

## `Control` Object

Each control object has a:
- Control Type (Binary, Threshold, Joystick)
- Condition (Complex)

## Control Types
Control types are just the types of controls you can have.\
Control Types:
- Binary - 0 Dimensional - Either pressed or not pressed
- Threshold - 1 Dimensional - Can be pressed with certain amounts of pressure (The reason I named it threshold is because you only make threshold conditions with it, so I named it that at first and never decided to change it)
- Joystick - 2 Dimensional - 

## Conditions
There are 2 types of conditions:
- Condition (The regular one)
- Complex Condition

Conditions are formatted as `<type>:<value>`
For example, x > 3 would be formatted as `GREATER_THAN:3` and x <= 5.2 would be `LESS_THAN_OR_EQUAL_TO:5.2`
However, you can only use condition types that go with your control type. (See below for condition types)
To have multiple conditions in one, you need to make a complex condition. These are all of your conditions
seperated by '|'s (Vertical Pipes)
Example: `GREATER_THAN:3|LESS_THAN_OR_EQUAL_TO:5.2`\
**Important!** All conditions within complex condition must be true for it to resolve! In other words, the '|' is an AND operator, not an OR one.
 
## Binary Controls
Binary Components:
- `A`
- `B`
- `X`
- `Y`
- `LB`
- `RB`

Condition types:
- `ACTIVE`
- `INACTIVE`

 
## Threshold Controls
Threshold components:
  - `LT`
  - `RT`

Condition types:
- `LESS_THAN:x`
- `LESS_THAN_OR_EQUAL_TO:x`
- `EQUAL_TO:x`
- `GREATER_THAN:X`
- `GREATER_THAN_OR_EQUAL_TO:x`
- `RANGE:x,y` (Inclusive, meaning it can equal x or y)  

## Joystick Controls
Joystick components:
- `A` (Leftmost joystick)
- `B` (Rightmost joystick)
- 
Condition types:
- `north`
- `northeast`
- `east`
- `southeast`
- `south`
- `southwest`
- `west`
- `northwest`
- `north:x`
- `northeast:x`
- `east:x`
- `southeast:x`
- `south:x`
- `southwest:x`
- `west:x`
- `northwest:x`
(**x = distance from center threshold, inclusive, meaning it will resolve to true if the distance is x or greater**)
(Put a '!' before the whole condition to invert it, to make things like !north, which would be true if it wasn't facing north)

## Controls, Error and Input Logger
**In order for these features to work, you must call `Controls.processAll()` in a loop, like `while(true)` or `robotPeriodic()`**

**ControlsLogger**
Logs output whenever it detects one of the control's conditions changes. (If one suddenly resolves or suddenly doesn't, it will log it)

**ErrorLogger**
Logs output whenever something goes wrong in the code. Useful for debugging issues with ControlsManager.\
(Mainly logs things whenever invalid arguments are provided for a method)

**InputLogger**
Logs output whenever it detects on of the controller's components (X,Y,LT,RT...) changes states.
Upon initialization of the Input Logger, you can suppress joystick output to keep the console output less cluttered in case you have some drift.

## Binded Functions
Binded functions are Consumer objects that store execution code for when certain control's conditions resolve.\
**In order for this feature to work, you must call `Controls.processAll()` in a loop, like `while(true)` or `robotPeriodic()`**

To bind a function to a control, use `Controls.bindFunctionToControl(...)`.\
Arguments:
- `name` - The control's name
- `executeOnceWhenBecomeTrue` - Weither not it should only execute the Consumer function exactly once when the condition becomes true, or if it should run the function every time processAll() gets called
- `function` - The consumer object. It requires at least one argument, so I made it a blank string that is completely useless. Look at Usage.java to see what that's all about.
