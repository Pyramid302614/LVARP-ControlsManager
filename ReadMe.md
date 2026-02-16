  # ControlsManager (Model 3)


To add this to a project, make sure to add package lines at the start of the files!


### Methods

`Controls.addBinary(name,control)` - Adds a binary control to the cache\
`Controls.addThreshold(name,control,condition)` - Adds a threshold control to the cache.\
`Controls.get(name)` - Fetches a control from the cache.\
`Controls.getJoystickAngle(a_or_b,controller)` - Gets Joystick A or B's angle. (For a_or_b, put "A" or "B", or "a" or "b")\
`Controls.getJoystickCondition(a_or_b,condition,controller)` - Gets weither not a joystick condition is true.\
`Controls.bindFunctionToControl(name,condition,onceOnTrue,function)` - Binds a function to a control when a condition is true.

 
### Binary Controls
It's either pressed or not, only needs 0D conditions
  - `A`
  - `B`
  - `X`
  - `Y`
  - `LB`
  - `RB`

 
### Threshold Controls
Gives a number, therefore requires 1D conditions
  - `LT`
  - `RT`

### Threshold Conditions
Threshold condition strings can contain one or more conditions to form
a Complex Condition. <Seperate conditions with '|' (Vertical Pipe)>

Condition types:
- `LESS_THAN:x`
- `LESS_THAN_OR_EQUAL_TO:x`
- `EQUAL_TO:x`
- `GREATER_THAN:X`
- `GREATER_THAN_OR_EQUAL_TO:x`
- `RANGE:x,y` (Inclusive, meaning it can equal x or y)  

Example complex conditions: (Condition Strings)
- `"LESS_THAN:30|GREATER_THAN_OR_EQUAL_TO:70.5"` --> x < 30, x >= 70.5
- `"EQUAL_TO:2.75|EQUAL_TO:2.78"`--> x == 2.75, x == 2.78
- `"RANGE:7,7.5"` --> 7 <= x <= 7.5

### Joystick Conditions
Joystick condition strings can only contain one condition.

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
