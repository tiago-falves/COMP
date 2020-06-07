# Compiler of the Java-- language to Java Bytecodes 

Project Evaluation

# GROUP: 3e
NAME1: Miguel Pinto, NR1: 201706156, GRADE1: 20, CONTRIBUTION1: 25%
NAME2: Nuno Cardoso, NR2: 201706162, GRADE2: 20, CONTRIBUTION2: 25%
NAME3: Pedro Esteves, NR3: 201705160, GRADE3: 20, CONTRIBUTION3: 25%
NAME4: Tiago Alves, NR4: 201603820, GRADE4: 20, CONTRIBUTION4: 25%

Given the features added to the compiler (which are detailed in the following sections), the group believes that a fair global grade would be 20 (approximately). A lot of work was put into this project and the group made sure to implement almost every bonus feature that was proposed by the professors, as well as every optimization option.

1. SUMMARY

The intention of this assignment was to develop a compiler which is able to translate Java-- (jmm) programs into java bytecodes. The compiler follows a well defined compilation flow, which includes: lexical analysis (using an LL(1) parser), syntactic analysis, semantic analysis and code generation. The code generation was also optimized using register allocation, constant propagation, constant folding for integers and booleans (as bonus) and by using templates for compiling while loops. Among these stages, it includes:

- Error treatment and recovery mechanisms
- Generation of a Syntax Tree (Abstract Syntax Tree)
- Generation of an LLIR (Low-Level Intermediate Representation)
- Generation of java bytecodes  


2. EXECUTE

To execute our project please run the script run.sh, which runs all the given tests with the given arguments:

- ./run.sh <input_file.jmm>

To add the flags, they have to be edited in the script.

To run a single File with the pretended flags, please insert the following command:

java –jar jmm.jar [-r=<num>] [-o] [-f] [-d] [-Winit] <input_file.jmm>


Debug mode 
- flag -d or --debug

Register Allocation Optimization mode 
- flag -r=N

Constant Propagation mode 
- flag -o

Constant Folding mode
- flag -f

Treat non-initialized variables as a warning
- flag -Winit or --InitializedWarning


3. DEALING WITH SYNTACTIC ERRORS

The compiler is able to skip a predefined number of errors.
Using the while_errors variable (in the file ParseException.java), the compiler is able to report at maximum while_errors errors, so that the programmer can then proceed with their correction.
This is done by skipping blocks of code whenever a new error is found, and incrementing a counter. 

4. SEMANTIC ANALYSIS

The compiler implements the following semantic rules:

- Variables:
    - Checks if all the variables including arrays are previously declared.
    - Checks if a variable is not defined more than one time.
    - Checks if variables are assigned to other variables with compatible types.
    - Verifies if a variable associated with a function call is a class type variable.
    - Checks if a variable is valid within a given scope.
    - Checks if a non-static variable is referenced in a static context.

- Functions:
    - Checks if the "this" keyword is not used in a static context.
    - Checks if the function called is compatible with any function (that is, a function having the same signature - number of arguments, as well as the type of those arguments).
    - Checks if the return value of a function can be assigned to a variable.
    - Checks if the return value of a function is initialized.
    - Checks if the return value of a function can be used in an expression.
    - Checks if a function of type void does not return anything.
    - Verifies additional types in the function return, including String, class variables and void.
    - Verifies if the function parameters have all different names.

- Arrays:
    - Checks if array expression is an integer and if it has been initialized.
    - Checks if the array access is always made in a variable of an array type (int[] or String[]).
    - Checks if the property length is only used in arrays.

- Block Statements (While and If..Else):
    - Checks if While and If have an expression that evaluates to a boolean.
    - Checks variable initialization inside if or else block.

- Classes:
    - Verifies the existance of a class when it is used.
    - When a class calls a function, verifies if the class variable is initialized and if it includes the function.
    - Verifies that a class is not instantiated inside an expression without any call to one of its functions. 

- Operations:
    - Verifies if conditional operations && (logical and) and ! (negation) are only used with boolean expressions.
    - Checks if conditional operator < (less than) is only used with arithmetic expressions or integers.
    - Verifies if an array is not directly used in an arithmetic or conditional operation (of type less than).


5. INTERMEDIATE REPRESENTATIONS (IRs)

The intermediate representation is being delivered by both the Syntax Tree (Abstract Syntax Tree) and the LLIR (Low-Level Intermidiate Representation). 
This representation is made after both the lexical and syntax are complete. Also, the IR help us structure the Java-- code in something simpler and more manageable. 
The creation of the LLIR also modularized our code to something closer the created Jasmin Code, becoming very easy to add new features and to apply optimizations as seen fit.

6. CODE GENERATION 

The Code generation is performed using as an input the LLIR, which is populated during the Semantic Analysis, as to make the process more efficient, rather that traversing the AST to analyse the semantics and then having to traverse it again to populate the LLIR.
The List of LLIRs is then transversed, each one having a correspondent Writer, which knows how to transform the LLIR in a JVM instruction. 
The code is all written to a file with the same name of the one recieved as an argument, but with a '.j' extention.
The code generation supports both static and non-static imports. It's important to mention this, given the fact that the given test files have only static import statements.

7. OVERVIEW

The development of the Java-- compiler followed the checkpoints presented in the project's description:
    1. Developed a parser for Java-- using JavaCC and taking as starting point the Java-- grammar furnished, using an LL(1) parser;
    2. Included error treatment and recovery mechanisms;
    3. Proceeded with the specification of the AST;
    4. Included the necessary symbol tables;
    5. Performed Semantic Analysis;
    6. Generated JVM code accepted by jasmin corresponding to the invocation of functions in Java--;
    7. Generated JVM code accepted by jasmin for arithmetic expressions;
    8. Generated JVM code accepted by jasmin for conditional instructions (if and if-else);
    9. Generated JVM code accepted by jasmin for loops;
    10. Generated JVM code accepted by jasmin to deal with arrays.
    11. Completed the compiler and tested it using a set of Java-- classes;
    12. Proceeded with the optimizations related to the code generation, related to the register allocation (“-r” option) and the optimizations related to the “-o” option and, as a bonus, the "-f" option.

Regarding the optimizations, both the "-o" option (Constant Propagation) and the "f" option (Constant Folding) used algorithms completely developed by the members of the group. In the "-o" option, any variable that can be considered as constant is printed on the screen at runtime. As for the "-r" option (Register Allocation with Liveness Analysis), the Dataflow and Liveness Analysis algorithms used were based on the ones presented in the theoretical classes. The Register Allocation was based on a graph-coloring algorithm defined in "Modern Compiler Implementation in Java" by Andrew Appel and Jens Palsberg. However, it does not contemplate spilling.


8. TASK DISTRIBUTION

The tasks were well distributed between all the peers in this work. 
All of us had a change to work in every topic. 
The work was passed around to keep everyone interested and to be able to correct errors from other classmates. 
It should be also noted that everyone impacted the work the same way and help to provide a stable and healthy group environment.


9. PROS

All the suggested stages for the compiler were followed and accomplished, resulting on a successfully implemented Java-- compiler. 
This project gave us a better insight vision of how a compiller works and processes the information. 
It should also be taken in account the amount of new information learnt over the course of the semester to build this compiler.

10. CONS

During the initial stages of the project, we did not realize how much code the compiler would need, not being very carefull about code organization in the beggining. 
This required a lot of refactoring and a complete change of mindset in the middle of the implementation of the project.