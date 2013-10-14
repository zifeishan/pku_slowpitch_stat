北大信科棒垒队技术统计器
====

本程序整理了2013年北大杯全校技术统计。

Peking University Slowpitch Softball -- Statistic Tool for aggregating the game records!

Authors: Zifei Shan, Yiyang Hao


How to use this
====

Running the program:
- By Eclipse: 
  - import into eclipse and run. 
  - If want to change directory, have to modify the program.

- By command line:
  - Enter root directory.
  - Compile: 

      mkdir bin
      javac src/SZFMain.java -d bin/

  - Execute:

      cd bin
      java SZFMain ../data/ ../data/input/

      (Or you can change directories: java SZFMain <data root directory> <data input directory> )
      

After running the program, output is generated in pitchers.txt and batters.txt.

Copy and paste the content into Excel templates.

Use sorting functions to sort.


Directory structure
====
.
├── README.txt
├── bin
├── data
│   ├── input                   -- input directory
│   ├── batters-template.xlsx   -- template to paste output to.
│   ├── pitchers-template.xlsx  -- template to paste output to.
│   ├── batters.txt             -- output
│   └── pitchers.txt            -- output
└── src
    └── SZFMain.java            -- main function
