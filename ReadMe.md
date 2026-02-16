  # ControlsManager (Model 3)


To add this to a project, make sure to add package lines at the start of the files!


### Methods

`Controls.addBinary(name,control,condition)` - Adds a binary control to the cache\
`Controls.addThreshold(name,control,condition)` - Adds a threshold control to the cache.\
`Controls.addJoystick(name,control,condition)` - Adds a joystick control to the cache.\
`Controls.get(name)` - Fetches a control from the cache.\
`Controls.getJoystickAngle(joystickControl)` - Gets Joystick A or B's angle.\
`Controls.getJoystickCondition(joystickControl,condition,controller)` - Gets weither not a joystick condition is true.\
`Controls.bindFunctionToControl(name,onceOnTrue,function)` - Binds a function to a control when it's condition is true.\
`Controls.conditionResolve(name)` - Gets weither not the control's condition is true.\
\
`Control.conditionResolve()` - Get's weither not that control's condition is true.

## `Control` Object

Each control object has a:
- Control Type (Binary, Threshold, Joystick)
- Condition (Complex)

## Control Types
Control types are just the types of controls you can have.\
Examples:
`Controls.BinaryControls.A`
`Controls.JoystickControls.B`
`Controls.ThresholdControls.RT`

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
 
### Binary Controls
Control types:
- `A`
- `B`
- `X`
- `Y`
- `LB`
- `RB`

Condition types:
- `ACTIVE`
- `INACTIVE`

 
### Threshold Controls
Control types:
  - `LT`
  - `RT`

Condition types:
- `LESS_THAN:x`
- `LESS_THAN_OR_EQUAL_TO:x`
- `EQUAL_TO:x`
- `GREATER_THAN:X`
- `GREATER_THAN_OR_EQUAL_TO:x`
- `RANGE:x,y` (Inclusive, meaning it can equal x or y)  

### Joystick Controls
Control Types:
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

### Binded Function Conditions
Binded Function conditions are called Super Conditions. That's right, they are
super!! They are no different than threshold complex conditions, other than how they
have condition types for all types of controls.

Threshold conditions:
- *(See section: Threshold Conditions)*

Binary conditions:
- `ACTIVE`
- `INACTIVE`
