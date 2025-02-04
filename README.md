# Timeline by Undalevein

Timeline is a completely esoteric 3D/2D and evil programming language. It involves really dastardly difficult way of thinking how to code on a 3D space using layers and an unreliable way of navigating through just to perform any computation.

In Timeline, you control a data pointer that points to a section in your code. There, you pick up items from infinity cells in which the accumulator that automatically stores and evaluates data and operators inside it. To get access to different values or operaetors, you must travel downwards and only downwards to get what you want. And make sure that you are not feeding the accumulator junk!

# Running Timeline

To run a timeline script, you will need the the latest version of Java (java 23.0.1 2024-10-15).

Clone the GitHub repository onto your machine. Then run the interpreter using the following command on your favorite command line (Note to replace [FILENAME] with a filename):

Bash

```sh
java -ea src\TimelineInterpreter.java < examples\[FILENAME].timeline
```

PowerShell

```ps1
Get-Content ..\examples\[FILENAME].timeline | java -ea .\src\TimelineInterpreter.java
```

To run an existing program as an example, run the following command:

Bash

```sh
java -ea src\TimelineInterpreter.java < examples\hi.timeline
```

PowerShell

```ps1
Get-Content ..\examples\hi.timeline | java -ea .\src\TimelineInterpreter.java
```

## Language Specifications

### Basic Rules

If you have touched Befunge before, the default rules are similar to Timeline.

The data pointer starts at (0,0) of your code, or the top left corner. By default, it travel right and it will keep moving right until it gets overridden (see **Static Cells** for more information).

The program begins by first performing an action on the character the data pointer is looking act, then evaluates the accumulator, then move. If a data pointer reaches either end of the row or column and steps out of bounds, it will loop around that same line or column.

### Layers

The programming language may seem like it's a 2D programming language, but topologically, it is a 3D programming language. Introducing: layers!

When a program runs, the data pointer will begin at layer 0.

The data pointer can go down the next layer by 1 when it is at the `@` character. This, in effect, will change what the Infinity Cells offer by 1. While this can allow you to get more values and operators, you can never go up, only down! And sure, because each Infinity Cell is looping you can reset all infinity cells not including the STDIN infinity cell; all you need to do is to go down to layer 43680... oh... oh no...

Infinity Cells are characters where if the data pointer initiates it, it will receive a value or operator depending on what layer number the data pointer is in. Take the character `D` as an example where it holds digit characters 0 to 9 then repeats. If the data pointer is at layer 0, then it will get `0` into the accumulator and if the data pointer is at layer 1, then it will get `1` and so on and so forth. Until the data pointer is at layer 10 is where the digits the accumulator can get wraps around, where the data pointer will receive `0` again if pointed. Keep this in mind when you have multiple different Infinity Cells in your code!

### Accumulator

In the program, you are controlling an accumulator. Internally, the accumulator stores 3 items: a left-value, an operator, and then a right-value. The accumulator begins at the left-value, then operator, then right-value.

The left-value and right-value are, internally, string type. However, they can behave similarly to other data types like integers, floats, or booleans. So, if you concatenate "1" and "0" to make "10" and then add "10" with "1", then it will be evaluated to "11".

The operator can only get operator types and they can be either unary or binary operators.

If the left-value or right-value is given an operator type or the operator is given a string type, then the accumulator will become AMORPHOUS. This means that the accumulator, if received any more items, will automatically evaluate to AMORPHOUS. It will remain AMORPHOUS until the accumulator is cleared by the clear cell `?` or this print cell `.`.

After evaluating the cell the pointer is on, the accumulator automatically evaluates before moving to the next character. It evaluates under the following conditions:

- If there's only the left-value (left-value is not empty), then no evaluation will be made.
- If there's a left-value and an unary operator, then an evaluation will be made.
  - If the left-value is incompatible with the operator (i.e. negating `"HELLO!"`), then the accumulator becomes AMORPHOUS.
  - If the left-value is compatible with the operator, it will perform the evaluation and save the result to the left-value and then removes the operator from the accumulator.
- If there's a left-value and a binary operator but the right-value is empty, then no evaluation will be made.
- If there's a left-value, a binary operator, and a right-value, then an evaluation will be made.
  - If either values are incompatible with the operator (i.e. adding `"M"` to `"9"`), then the accumulator becomes AMORPHOUS.
  - If both values are compatible with the operator, it will perform the evaluation and save the result to the left-value and then removes the operator and the right-value from the accumulator.

#### Numbers

As mentioned, although everything is a string, they can still be inferred as integer or float types. When evaluating using operators that require integers or float values, it will first evaluate the values as integers (if available) and the evaluate the values as floats.

A string is inferred as a integer if all of characters are digit character. A string can still be an integer if the first character is a minus symbol, but it must have digit characters after it.

Examples of integers: `154`, `-4231`

A string is inferred as a float if all characters are a digit character and exactly one period somewhere in the string. Like the integer, a string can still be a float if the first character is a minus symbol, but it must have digit characters and a dot after it.

Examples of floats: `356.0`, `-0.1`

#### Booleans and Truthiness

Booleans are `"TRUE"` and `"FALSE"`. These are results after using a successful relational opereator evaluation.

Technically, you can make the word `"FALSE"` and it will be considered as false internally, though I am not sure why you want to do that.

In some scenarios when a boolean is required, the interpreter will evaluate things as truthy or falsy. Everything is considered truthy except for the following:

- The left-value is `"FALSE"` (all capitalized).
- The left-value is empty.
- The accumulator is AMORPHOUS.
- The accumulator contains a binary operator that it can't evaluate yet.

However, these below are not considered falsy.

- Left-value is `"AMORPHOUS"`, including its lowercase variations, but the accumulator is not AMORPHOUS.
- Left-value is `"0"`.
- Left-value is any of the lowercase variations of of `"FALSE"`.

### Coding Specifications

Below are the characters that are used in the program. Any other character not listed are considered comment characters.

#### Infinity Cells

Infinity cells are cells that contain all of the necessary values and operators that can perform computation. Additionally, there are characters that changes the direction of your data pointer. If your data pointer is pointing at one at a given moment, it will add that item to your accumulator based on your layer number.

| **Character** |                  **Term**                  |                                                                                                            **Order**                                                                                                             |
| :-----------: | :----------------------------------------: | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|      `A`      |                  Booleans                  |                                                                                                     `TRUE` -> `FALSE` -> ...                                                                                                     |
|      `B`      |             Boolean Operators              |                                                                                                  `not` -> `and` -> `or` -> ...                                                                                                   |
|      `C`      |          Concatenation Operators           |                                                                                                   `concat` -> `repeat` -> ...                                                                                                    |
|      `D`      |                   Digits                   |                                                                                          `0` -> `1` -> `2` -> ... -> `8` -> `9` -> ...                                                                                           |
|      `E`      |            Relational Operators            |                                                                                        `==` -> `!=` -> `<` -> `<=` -> `>` -> `=>` -> ...                                                                                         |
|      `I`      |                Stdin Input                 |                                                                                        `Stdin[0]` -> `Stdin[1]` -> ... `Stdin[n]` -> ...                                                                                         |
|      `L`      |             Lowercase Letters              |                                                                                          `a` -> `b` -> `c` -> ... -> `y` -> `z` -> ...                                                                                           |
|      `M`      |            Arithmetic Operators            |                                                                                   `+` -> `-` -> `*` -> `/` -> `**` -> `%` -> `negation` -> ...                                                                                   |
|      `N`      |             Bitwise Operators              |                                                                                        `~` -> `&` -> `\|` -> `<<` -> `>>` -> `>>>` -> ...                                                                                        |
|      `R`      |            Estimation Operators            |                                                                                          `round` -> `ceil` -> `floor` -> `trunc` -> ...                                                                                          |
|      `S`      |                  Symbols                   | `` ` `` -> `~` ->`!` -> `@` -> `#` -> `$` -> `%` -> `^` -> `*` -> `(` -> `)` -> `-` -> `_` -> `=` -> `+` -> `[` -> `{` -> `]` -> `}` -> `\` -> `\|` -> `;` -> `:` -> `'` -> `"` -> `,` -> `<` -> `.` -> `>` -> `/` -> `?` -> ... |
|      `T`      |           Trigonometry Operators           |                                                                                 `sin` -> `cos` -> `tan` -> `csc` -> `sec` -> `cot` -> ... -> ...                                                                                 |
|      `U`      |             Uppercase Letters              |                                                                                              `A` -> `B` -> ... -> `Y` -> `Z` -> ...                                                                                              |
|      `W`      |                 Whitespace                 |                                                                                                 ` ` -> `\n` -> `\t` -> ...-> ...                                                                                                 |
|      `a`      | Movement 1 (Changes Accumulator Direction) |                                                                                            `LEFT` -> `DOWN` -> `RIGHT` -> `UP` -> ...                                                                                            |
|      `b`      | Movement 2 (Changes Accumulator Direction) |                                                                                            `DOWN` -> `RIGHT` -> `UP` -> `LEFT` -> ...                                                                                            |
|      `c`      | Movement 3 (Changes Accumulator Direction) |                                                                                            `RIGHT` -> `UP` -> `LEFT` -> `DOWN` -> ...                                                                                            |
|      `d`      | Movement 4 (Changes Accumulator Direction) |                                                                                            `UP` -> `LEFT` -> `DOWN` -> `RIGHT` -> ...                                                                                            |

#### Static Cells

Static Cells do not change when going down layers.

| **Character** |                                                                                                                 **Description**                                                                                                                  |
| :-----------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|      `>`      |                                                                                                         Turn clockwise unconditionally.                                                                                                          |
|      `<`      |                                                                                                      Turn counterclockwise unconditionally.                                                                                                      |
|      `)`      |                                                                                     Turn clockwise if the accumulator is true. (Does not clear accumulator)                                                                                      |
|      `(`      |                                                                                    Turn counterclockwise if accumulator is true. (Does not clear accumulator)                                                                                    |
|      `@`      |                                                                                                            Enter the next layer by 1.                                                                                                            |
|      `#`      |                                                                                                        Hop over the cell in front of you.                                                                                                        |
|      `0`      |          Storage. Drop a copy of the left-value on the same layer on the character. Won't drop if the accumulator is empty or AMORPHOUS. Will pick up the item to either the left or right value once. Can be reused on the same layer.          |
|      `1`      | Storage. Drop a copy of the left-value down 1 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `2`      | Storage. Drop a copy of the left-value down 2 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `3`      | Storage. Drop a copy of the left-value down 3 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `4`      | Storage. Drop a copy of the left-value down 4 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `5`      | Storage. Drop a copy of the left-value down 5 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `6`      | Storage. Drop a copy of the left-value down 6 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `7`      | Storage. Drop a copy of the left-value down 7 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `8`      | Storage. Drop a copy of the left-value down 8 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `9`      | Storage. Drop a copy of the left-value down 9 layer on the character. Won't drop if the accumulator is empty or AMORPHOUS or if a value has already been dropped down before. Will pick up item the item to either the left or right value once. |
|      `?`      |                                                                                                              Clears the accumulator                                                                                                              |
|      `.`      |    Prints the left-value from the accumulator only if the value in the accumulator is not AMORPHOUS or unevaluated. If it is AMORPHOUS, it will print AMORPHOUS. If it has not been evaluated, it will print UNEVALUATED. Clears afterwards.     |
|      `,`      |              Prints the left-value from the accumulator only if the value in the accumulator is not AMORPHOUS or unevaluated. If it is AMORPHOUS, it will print AMORPHOUS. If it has not been evaluated, it will print UNEVALUATED.              |
|      `X`      |                                                                                                             Terminates the program.                                                                                                              |

### Turing Completeness

Due to how complex it is to code in Timeline, it is unclear whether or not the program is Turing Complete. As such, I am open to feedback and willing to update the language if it is not Turing complete!
