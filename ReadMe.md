# ControlsManager Model 4

## Adding this to your project
To add this to a project, simply take the files and plop them in somewhere, and then update the package.____ in each file.

## Terminology
A **Control** is a condition tied to a controller component that has to resolve for something to happen.
A Controller **Component** is a button/trigger/bumper/joystick on a Controller.
A **Controller** is a physical input interface that has a collection of buttons or sticks I guess.

## Values
**Binary:** 0.0 or 1.0 (False or True)
**Threshold:** -1.0 <-> 1.0 (Not held <-> Held) (For the only threshold components on a controller, the Triggers)
**Joystick:** x: -1.0 <-> 1.0 (Left <-> Right) y: -1.0 <-> 1.0 (Up <-> Down)

## Conditions
Conditions are stored like this:
`"<TYPE>:<VALUE>|<TYPE>:<VALUE>|..."` (A condition can only resolve if ALL of the individual conditions seperated by '|' (Pipes) resolve.)
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
"Why is it lowercase?" - me, february 23rd, 2026
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
