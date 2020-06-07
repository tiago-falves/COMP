# Compiler of the Java-- language to Java Bytecodes [Group 62]

 Project Evaluation

All the grades listed below are provided on a scale of 1 to 20.

|      | Name           | Number    | Self Assessment | Grade 1            | Grade 2            | Grade 3            | Grade 4            | Contribution (%) |
| ---- | -------------- | --------- | --------------- | ------------------ | ------------------ | ------------------ | ------------------ | ---------------- |
| 1    | Miguel Pinto   | 2017xxxxx | 19              | ------------------ | 19                 | 19                 | 19                 | 25               |
| 2    | Nuno Cardoso   | 2017xxxxx | 19              | 19                 | ------------------ | 19                 | 19                 | 25               |
| 3    | Pedro Esteves  | 2017xxxxx | 19              | 19                 | 19                 | ------------------ | 19                 | 25               |
| 4    | Tiago Alves    | 201603820 | 19              | 19                 | 19                 | 19                 | ------------------ | 25               |

Given the features added to the compiler (which are detailed in the following sections), the group believes that a fair global grade would be __19__ out of 20.

1. Summary

The intention of this assignment was to develop a compiler, named _jmm_, which is able to translate Java-- programs into java _bytecodes_. The compiler follows a well defined compilation flow, which includes: lexical analysis (using an __LL(1)__ parser), syntactic analysis, semantic analysis and code generation. The code generation was also optimized using register allocation, constant propagation and by using templates for compiling while loops. Among these stages, it includes:

- Error treatment and recover mechanisms
- Generation of a Syntax Tree (_Abstract Syntax Tree_)
- Generation of an LLIR (Low-Level Intermediate Representation)
- Generation of java _bytecodes_ 
- 


2. Execution

To execute our project please run the script run.sh, which runs all the given tests with the given arguments:

- ./run.sh <input_file.jmm>

To add the flags, they have to be edited in the script.

To run a single File with the pretended flags, please insert the following command:

java –jar jmm.jar [-r=<num>] [-o] <input_file.jmm>


Debug mode 
- flag -d or --debug

Register Allocation Optimization mode 
- flag -r=N

Constant Propagation mode 
- flag -o

Treat non-initialized variables as a warning
- flag -Winit or --InitializedWarning


3. Dealing with Syntactic Errors

TODO MUDAR
The compiler is able to skip a predefined number of errors.
Using the _errors_ variable (in the file JavammJJTWhileStatemet.jjt_), the compiler is able to report _errors_ errors, so that the programmer can then proceed with their correction.
This is done by skipping blocks of code whenever a new error is found, and incrementing a counter. 

4. Semantic Analysis

The compiler implements the following semantic rules:

- Check if the return value of a function is ever initialized.

- Check if variables are assigned to other variables with compatible types.

- Check if the function called is compatible with any function (that is, a function having the same signature - number of arguments, as well as the type of those arguments).

- Check if the return value of a function can be assigned to a variable.

- Check if a variable is valid within a given scope.

- Checks if array expression is an integer and if it has been initialized.

- Checks if a non-static variable is referenced in a static context

- Check if the return value of a function can be used in an expression.

- Check if a variable is not defined more than one time.

- Checks if While and If expression evaluates to a boolean

- Assumes the return value of a function it doesn't know to the variable it is beeing assigned or assumes it is void if not being assigned to anything

- Checks variable initialization inside if or else block

- When a class calls a funciton, verifies if the class variable is initialized and if it includes the function.

- Verifies additional types in the function return, including String, class variables and void.

5. Intermediate Representations (IRs)

The intermediate representation is being delivered by both the Syntax Tree (_Abstract Syntax Tree_) and the LLIR (_Low-Level Intermidiate Representation). This representation is made after both the lexical and syntax are complete. Also, the IR help us structure the Java-- code in something more simpler and manageable. The creation of the LLIR also modularized our code to something closer the created Jasmin Code, becoming very easy to add new features and to aplly new optimizations.

6. Code Generation 

The Code generation is performed using as an input the LLIR, which is populated during the Semantic Analysis. The List of LLIRs is then transversed, each one having a correspondent Writer, which knows how to transform the LLIR in a JVM instruction. The code is all written to a file with the same name of the one recieved as an argument, but with a _.j_ extention.

TODO REFERIR OS PROBLEMAS QUE A CODE GENERATION PODE TER

7. Overview

TODO O QUE POR AQUI?

refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)

8. Task Distribution

The tasks were well distributed betweed all the peers in this work. All of us had a change to work in every topic. The work was passed around to keep everyone interested and to be able to correct errors from other classmates. It should be also noted that everyone impacted the work the same way and help to provide a stable and healthy group environment.


Pros

All the suggested stages for the compiler were followed and accomplished, resulting on a successfully implemented Java-- compiler. This project gave us a better insight vision of how a compiller works and processes the information. It should also be taken in account the amount of new information learnt over the course of the semester to build this compiler.

Cons

During the initial stages of the project, we did not realize how much code the compiler would need, not being very carefull about code organization in the beggining. This required a lot of refactoring and a complete change of mindset in the middle of the implementation of the project.
