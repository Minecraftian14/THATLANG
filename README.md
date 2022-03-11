## THATLANG

### That Programming Language.

```yaml
program main:
  println("Hello World!")
```

[![](https://img.shields.io/discord/872811194170347520?color=%237289da&logoColor=%23424549)](https://discord.gg/hZnHFGvU6W)

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

#### Program Files

A collection of programs, functions and other components of this level written in a single file is called a Program
File. It may look like it has a single program, but it actually refers to the main program kept in there.

This is basically a very loose area where something wrong might just not get parsed, and we wish to keep it like that.
In fact, this is especially true for the end of the file.

Regarding the impact on execution, the only exceptional property this part of THATLANG is the existence of two program
named `init` and `main`. `init` is called when the file is loaded, and main is called when the "file is ran".

Given a VM, any of the programs can e called first (but after init) using their name.

#### Programs

A program is the smallest unit of logical code structures which can independently be run in a VM. It consists of a
linear series of statements which are ran one after another. The execution is almost liner, like except for a few cases
like the use of `goto`

Any project made in THATLANG must have at least one program, and if it's supposed to be an application, it must have a
program called main. Basically, if a program file has a main program, 'running' file itself will automatically run the
main program. However, note, that other programs can also be called whenever necessary, by providing the name of the
program to the VM.

Every program has its own scope (though there can be multiple scopes, refer `launch program`), so, by default any
variable declared inside the program can only be accessed within the execution of that program. However, these variables
can be passed to `functions`, which can mutate them. Or the function ca also inject new variables into the current
scope. If the existing program calls another program,a new scope to treat the new program is developed. When the new
program ends, the older one gets its scope back. Note, that not the same for multithreaded execution!

Ideally, each program is expected to perform a specific task which doesn't depend on others, though that's of course not
possible. That's why some programs can call others using program action keywords to get some work done. However, there's
no defined way of interaction, communication and dependence propagation among a set of programs calling each other.
Instead, the programs must rely on using `context` structure to store globally accessible variables.

Like most functions in other programming languages, specifically the main function, if they call `return 0` or
equivalent, the execution stops without peeking at further statements. Currently, there's no such thing in THATLANG
because programs are not meant to return anything. However, it is possible to end execution completely for the project
by calling `exit()` or one can also start another program using `end with` program action to stop only the current
program.

[//]: # (// TODO: Implement sealed and permits, which defines which context is accessible by how many programs, and which program is allowed to be ran by which programs.)

[//]: # (// TODO: Also implement private programs which are accessible only within a file.)

[//]: # (Include a few points about ghost scope, the temporary scope made by a function.)

#### Functions


### Quick Start

#### Program Files

```yaml
program prog_name:
  pln("statement 1")
  pln("statement 2")
```
```java
program prog_name {
  pln("statement 1")
  pln("statement 2")
}
```
```js
program prog_name ->
  pln("statement 1")
```

#### Programs

A program can

### THATLANG Pangram

## Terminologies

- NATA: Not At all a programming language, and don't ask why there's a T before the last A.
- LOOP: Literally 'Object-Oriented' Programming
- LFAS: Language Features and Specs
- Linear Code:
- MAT: Multi Assignment Thing
- POP: Pointers-Oriented Programming
