# CPT204 Ataxx on AI

Ataxx was invented in 1988 by David Crummack and Craig Galley (originally named Infection) as a board game with very simple rules to be played against a computer.

In this project, as it is a part of coursework for Oriented-object Programming (OOP), it deployed some concepties and features of OOP. Specifically, it used deep copy, wapper, interface, abstract class to support the whole implementation of game strategy in Ataxx.

The used algorithm in this game is based on **Negamax algorithm** which a famous and popular algorithm in the **Alpha-Beta Pruning** method. It will dynamically optimize current decision by evaluting the final results in a few future steps. Of course, more deeper the perdiction is, larger the probability for winning game will be.

But considering the time cost, which is crucial in the real-time game, the AI is asked to stop calculation and give temporary optimal result in 5 seconds. So there will be a tradeoff between the operation time and accuracy of computation result.
