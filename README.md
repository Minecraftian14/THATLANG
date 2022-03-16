## THATLANG

### That Programming Language.

```yaml
program main:
  println("Hello World!")
```

[![](https://img.shields.io/discord/872811194170347520?color=%237289da&logoColor=%23424549)](https://discord.gg/Ar6Zuj2m82)

#### Quick Links

- [Depth](#Theoretical Lecture)
- [Quick Start](#Quick Start)

### Theoretical Lecture

THATLANG is a personal project made with the aim of following the NATA principal. This document is likely to follow a
lot of terminology, please refer the list given [here](#Terminologies)

The language's base mechanism is directly influenced from LOOP principal, so almost everything is an object. Those which
are not yet objects, are meant to be made objects some day.

Some specific aims and LFAS which directed the flavour of THATLANG:

- Be as flexible as possible. Introduce multiple syntax to support a wild variety of features and specs which are
  popular.
    - One of the most used way of declaring code blocks are the braced blocks. Specifically, a braced block is a part of
      linear code enclosed with
      '{' and '}' which consists of statements. Another popular type would be the Indented Block, where statements are
      written in a span defined by the number of indents each statement starts with. The only type of indented block in
      my knowledge are initiated using a ':' and then all following statements with a constant indent are included in
      the block. Therefore, THATLANG allows one to use both types of code blocks!
      ```yaml
      Some Declaration:
        Some Statements
        Some Statements
        Some Statements
      ```
      ```js
      Some Declaration {
         Some Statements
          Some Statements
        Some Statements
      }
      ```
      In fact, THATLANG also includes an experimental code block method which allows only one statement to exist within
      it, and the statement can be placed however you like, Ideally, if the statement is something like an if-else, they
      both will be included.
      ```js
      Some Declaration 1 ->
         Some Statement
      Some Declaration 2 ->
      Some Statement
      Some Declaration 3 -> Some Statement
      ```
    - As usual, every programing language has its own challenges it wants to tackle. Here we aim to make object creation
      easier. By object creation we mean, creating an object, setting its fields, and even using it. We think the most
      easy way so far just directly accessing fields using a '.'. Therefore, `something.that` instantly creates a new
      object called that inside something. Not only that, we introduce a new way of defining objects, limited to only
      allow objects named with a single character called MAT expression.
      ```
      // The following line creates three objects named a, b and c with the values 1, 2 and 3
      object .abc 1 2 3
      // This is probably a useful thing when we have to assing similar properties for an object, for instance, when 
      // creating a box, it have several properties like it's abscissa, ordinate, width, height and color.
      // The following line:
      UI().box() .xy 0 0 .wh 100 100 .rgba 1. 1. 1. 50 
      // is equivalent to:
      val box = UI().box()
      box.x = 0
      box.y = 0
      box.w = 100
      box.h = 100
      box.r = 1.0
      box.g = 1.0
      box.b = 1.0
      box.a = 50
      ```
    - Almost every language has a way of defining a specific part of the code which can be run again and again, often
      with specific constraints called arguments. These, so-called functions, may be also return a few results of their
      actions. Considering how important they are, THATLANG also directed towards making functions a thing. However, we
      had certain options while doing so. Some languages allow only one return value, for instance Java, while some
      languages prefer a POP method allowing one to send pointers to functions, and letting functions mutate the
      original object as much as they like. But we all know that pointers are bad. so allowing POP is also not a good
      thing. And then comes C# which does an even more hacky-but-better way of handling multiple returns, and that is,
      using the out keyword to specify which object is going to be mutated instead. Not a fan of these ideas, but Python
      did show us something interesting! Simply said, it can just return a tuple of all the objects required. And with
      that, it didn't take much time, before proving itself to be a great utility! However, THATLANG was still not
      satisfied... Even though it's an interpreted language, it felt that creating an object to store more objects is
      probably not a good thing. That's where the concept of injections rose! The idea is, that when a function is
      called, it simply injects the required variables into the calling source.

Now, let's proceed to learn the various parts of a THATLANG program.

[//]: # (#### Program Files)

[//]: # (#### Programs)

[//]: # (#### Functions)

### Quick Start

#### Program Files

Compose a bunch of Programs, Functions, Contexts and other components of this level, and just place them one after
another.

As such order doesnt matter, but it's nich to put context above the programs which uses it, and functions after the
programs which uses it.

#### Program

```yaml
program prog_name:
  pln("statement 1")
  pln("statement 2")
```

```java
program prog_name{
        pln("statement 1")
        pln("statement 2")
        }
```

```js
program prog_name ->
  pln("statement 1")
```

#### Function

A function in its simplest form.

```yaml
func printSomething():
  println("Something...")
```

A function which takes in two arguments.

```yaml
func printSomething(arg1, arg2):
  print(arg1)
  println(arg2)
```

A function which returns two values.

```yaml
func ret1, ret2 printSomething():
  println("Something...")
  ret1 = "Some Value"
  ret2 = "Another Value"
```

A function which takes in two arguments and returns two values.

```yaml
func ret1, ret2 printSomething(arg1, arg2):
  println(arg1 + arg2)
  ret1 = "Some Value"
  ret2 = "Another Value"
```

Using functions

```yaml
program main:
  // Simple call
  defineVariables(45)
  pln(" a = %.1f, b = %.1f" % [a, b])

  // A Binary call
  a operatesOn b
  pln(" a = %.1f " % a)

func a, b defineVariables(num):
  a = 3.5 * num
  b = 6.2 * num

func operatesOn(x, y):
  x = x + y
```

Named arguments

```yaml
program main:
  printThis("Hello", "World")
  printThis(second="World", first="Hello")

func printThis(first, second):
  p("%s %s!%s" % [first, second])

```

#### Context

```yaml
context someContext:
  varA = "A"
  varB = "B"
```         

#### Creating and Assigning Variables

Simply said, one can just write the line `var name = value` to create a variable named `name`.

To reassign a variable, one can just `name = value`.

To redefine a variable, one can write the declaration again, `var name = value`.

```yaml
context variableTutorial:
  var aStringVariable = "A String Value"
  var anIntegralVariable = 175358
  var aDecimalVariable = 175.358

  aDecimalVariable = 1e.497
  // Equivalent to 1 * 10^0.497 which approximately equals to 3.14

  aStringVariable = """
  A multiline string.
  """
```

#### Comments

```yaml
context commentsTutorial:
  // A single line comment

  /*
  A block comment
  */

  /**
  * And a box comment.
  * It is not any different than a block comment,
  * except that it can be made different.
  * (for example, language addons or IDE integrations)
  **/

  // This is a tabular comment.
  /============================#=================\
  |     Types Of Comments      | Is Single Line? |
  |----------------------------|-----------------|
  | Single Line Statement      |       Yes       |
  | Inline (TBD)               |       Yes       |
  | Block Comment              |       No        |
  | Box Comment                |       No        |
  | Tabular Comment            |       No        |
  | External Compiler (TBD)    |       No        |
  \============================^=================/
```

#### External Language Reference

Use other modes like YAML and JSON to create complex objects.

```yaml
context variableTutorial:
  var anObject = <%
  <!DOCTYPE json>
  {
  "field1": 1289,
  "field2": { "value": [ 1987 ] }
}
  %>

  pln(something.field1)
  pln(something.field2.value[0])

  var something = <%
    <!DOCTYPE yaml>
    ---
    field1: 1289
    field2: {value: 1987}
    field 3:
      that thing:
        - Hello
        - World
    ...
  %>

  pln(something[0].field1)
  pln(something[0].field2.value)
  pln(something[0]["field 3"]["that thing"][1])
```

#### If Flow

```yaml
context ifFlowTutorial:
  // Braces are optional.
  if (isRunning) -> doThat()
  // is equivalent to
  if isRunning -> doThat()

  // Simple If
  if age < 12:
    addChildrenContent()

  // If with else tail
  if age < 12:
    addChildrenContent()
  else:
    addTeenContent()
  // one may also write
  or else:
    addTeenContent()

  // Chained If Flow
  // One can use 'else if' or 'elif'
  if day equals "Sunday":
    justSomeCodePutInForExample()
  else if day equals "Saturday":
    iWonderWhenItWillOver()
  elif day equals "Wednesday":
    ahhTodayHasntBeenGood()
  getACoffee()
```

#### Multi Assignment Statement (MAT)

The basic use involves writing the name of the base object, giving a few spaces, and then a word starting with `.`. This
word refers to just as many variables as the letters in it. And then, one need to add just as many values after that.

For instance `base .xy`

Note, one can only create/assign single-character-ly named variables inside an object.

```yaml
context MAT_Tutorial:
  base .xywh 1 2 3 4
  // is equivalent to
  base.x = 1
  base.y = 2
  base.w = 3
  base.h = 4

  // This notation can be split apart to make things more clear
  base .xy 1 2 .wh 3 4

  // There can be any number of spaces in between,
  // only a minimum of 1 space is required between each token
  base   .xy     1     2
  base   .wh   190    45

  // The base may be an object in reference, or an expression
  getRectangle().pos .xy 0 0

  // This is especially useful in the UI functions
  // UI().box() returns a new filled box
  UI().box() .xy 0 0 .wh 1f 1f .rgb 0 1f 0
```

#### Collections

```yaml
context collectionsTutorial:
  // Create an ArrayList
  var arrayList = [1, 2, 3, 4]
  // Create a LinkedList
  var linkedList = l[1, 2, 3, 4]
  // Create a HashSet
  var hashSet = {1, 2, 3, 4}
  // Create a HashMap
  var hashMap = {"a":1, "b":2, "c":3, "d":4}
  // Create a Hashtable
  var hashtable = t{"a":1, "b":2, "c":3, "d":4}
  // Create a Stack
  var stack = |1, 2, 3, 4<
  // Create a ArrayDeque
  var stack = <1, 2, 3, 4<

  // To acces an element from most of these data types
  println(arrayList[2])

  // One can provide arguments for formatting to a string literal using collections.
  println("I like %s, I have like %d types of these." % ["Glitters", 7])

  // Adding/removing elements is still a work in progress, it will soon be supported.
```

#### User Interface

Call the UI function to initialise the current program with a UI window.

`UI()`

Optionally one can give the name, width and height as arguments.

`UI("A Good Name", 1920/2, 1080/2)`

The UI function returns a UI context object, which contains more functions to aid in simple drawings.

Three types of values are recognised when given to a drawable object like box.

- A Positive Integer
    - Here the value is assumed to be in quantity of the smallest quanta possible. For instance in case of position,
      it's one pixel, in case of color, it's one bit (the numbers 0 to 255).
    - In case of position, it's calculated from top-left.
    - In case of color, it's calculated from 0.
- A Non-Negative Real Number
    - In this case the value is assumed to be in relative of the maximum possible. The maximum depends on the case, for
      instance for position, it's the sixe of parent object, and in color, it's 255. A value of 0.0 means 0 and a value
      of 1.0 means the max. Following the pattern, o.5 means half the max.
    - In case of position, it's calculated from top-left.
    - In case of color, it's calculated from 0.
- A Negative Real Number
    - This time the value is made positive and calculated just like a non-negative real number.
    - In case of position, it's calculated from bottom-right.
    - In case of color, it's calculated from 255.

```yaml
program main {
  UI("Example", 500, 500)

  // Draw a rectangle
  UI().box() .xy -0.05 0 .xy 1.   1. .rgba 1. 1. 1. 50
  UI().box() .xy  0    0 .xy 0.05 1. .rgba 1. 1. 1. 50

  // Draw an oval
  UI().oval() .xy 0 0.9 .xy 1. 1.1 .rgba 1. 1. 1. 50

  // Draw a polygon
  UI().poly(0,0, 0.1,1, 0.9,1, 1,0) .xy 0 0 .wh 1. .05 .rgba 1. 1. 1. 50

  // Create a Button
  UI().button(onclick=act()) .xy 0.1 0.1 .wh 0.3 0.09

  // Create a Field
  UI().field(text="enter smth") .xy 0.1 0.2 .wh 0.3 0.09

  // Create a Check Box
  UI().check(text="Do you want it?") .xy 0.1 0.3 .wh 0.3 0.09

  // Create Radio Buttons
  val rA = UI().radio("Option A") .xy 0.1 0.4 .wh 0.19 0.09
  val rB = UI().radio("Option B") .xy 0.3 0.4 .wh 0.19 0.09
  val rC = UI().radio("Option C") .xy 0.5 0.4 .wh 0.19 0.09
  UI().group(rA, rB, rC)

  UI()
  sleep(5000000)
}

function act():
  prtln("Button Pressed!")
```

#### Operators supported

THATLANG provides an enormous variety of operators, which even include words like `nand`, `xor`, `equals`, `not equals`
and so on. It's hard to maintain the list here, so it's best to see the source code itself.

[THOSE Operators](src/main/java/thatlang/core/THOSEOperatorsPrototype.java)

Note, that the operators processing is still an experiment, so far, it just works. Actually, I want a better way to
decide an operator based on the right and left operands. "Better" as in? With a total of 8 types of primitive variables,
a few other operable variables like `BigInteger` and `String`, and other objects which may define a function for an
operator for themselves, it's really hard and boring to write the same code for each kind of possible input.

Maybe, some magic using annotation processor can make the source code more manageable and cleaner, but that's another
big project to create. Maybe, it will happen someday (but not any soon).

Meanwhile, We would love to get suggestions or contributions in any kind of feature for THATLANG.  

[//]: # (### THATLANG Pangram)

## Terminologies

- NATA: Not At all a programming language, and don't ask why there's a T before the last A.
- LOOP: Literally 'Object-Oriented' Programming
- LFAS: Language Features and Specs
- Linear Code:
- MAT: Multi Assignment Thing
- POP: Pointers-Oriented Programming
