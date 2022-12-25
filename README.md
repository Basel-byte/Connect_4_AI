# ***Connect-4 :space_invader:***

- This project was generated with JAVAFX to solve 8-puzzle to goal state 012345678.


## Authors:
> [Habiba Osama zaky](https://github.com/habibaosama)
>
> [Mai Ahmed Hussien](https://github.com/MaiAhmedHussein)
>
> [Basel Ahmed Awad](https://github.com/Basel-byte)
>
> [Ali Hassan Alsharawy](https://github.com/AliELSharawy)  

## UI
![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/first%20page.png)

## Table of Contents

- [User Guide and features](#User-Guide-and-features)
- [Heuristic used for Minimax Algorithm](#Heuristic-used-for-Minimax-Algorithm)
   
    

## User Guide and features 

- ***First home screen :***

   - The user chooses to play with `computer` or `two players`


- ***Second Game scene :***

   - The user defines the number of `levels` for the minimax algorithm max level for without pruning **8** and  with pruning **10**

   - Choose to play with `pruning` or `without pruning`
   
    ![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/2nd%20page%20of%20single%20player.png)

- ***Third Game Scene :*** 
   - The `score` of the computer and the player score are shown

   - The `Restart` button 

   - `Reset` the game and start with the same initial conditions

   - The `Back` button go back to home

    ![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/playing%20page.png)


   - The `showTree` button view the tree of the evaluation of the minimax algorithm  where the red arrow indicates the chosen column


    ![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/without%20pruning%20tree.png)


      With pruning the **X** means the this branche is cut off
    ![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/with%20pruning%20tree.png)
     



## Assumptions:
 - our game the player continue playing **until the board is full** 

 - The cpu is **red** player

 - Consider **overlapping** as points (if there is five piecies beside each othermeans 2 points)


    ![image](https://github.com/habibaosama/Connect_4_AI/blob/main/images/overlapping%20points.png)


## Heuristic used for Minimax Algorithm
   
- Check every group of 4 in the whole board (horizontally, vertically, in the positive diagonal, int the negative diagonal) and certain combinations of the 4 group has a weight:
   - **4 computer pieces** (point) -> 100

   - **3 computer pieces and an empty place** (candidate point)-> 20

   - **2 computer pieces and an empty place** (candidate point)-> 6

   - **4 opponent pieces** (point) -> -150

   - **3 opponent pieces and an empty place** (candidate point)-> -60

   - **2 opponent pieces and an empty place** (candidate point) -> -6

   - **middle column** -> 2 for every piece

   - **2 columns beside middle column left and right of it** -> 1 for every piece
   (as the middle col increase the chance for the cpu to win more points)



- Algorithm is **defensive**


- The evaluation called at the `terminal nodes`.
When the terminal state is reached, we call the `evaluateScore` method from the `Evaluation class`. This method evaluates the whole heuristic.
 
