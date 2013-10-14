北大信科棒垒队技术统计器
====

本程序整理了2013年北大杯慢投垒球技术统计。

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

  - Or you can change directories: 
  
        java SZFMain <data root directory> <data input directory> 
      

After running the program, output is generated in pitchers.txt and batters.txt.

Copy and paste the content into Excel templates.

Use sorting functions in Excel to sort & see results.


Directory structure
====

- README.txt
- bin
- data
    - input                   -- input directory
    - batters-template.xlsx   -- template to paste output to.
    - pitchers-template.xlsx  -- template to paste output to.
    - batters.txt             -- output
    - pitchers.txt            -- output
- src
    - SZFMain.java            -- main function


记录格式
====

记录要准确反映：

a. 每一棒的打击员是"谁"：如果有换人，棒次可能还原出现错误。建议记录员除棒次之外写明姓名或球衣背号！
b. 每一棒是安打还是靠防守队员失误上垒。这个对打击数据是很重要的，所以“1棒上一垒”这样的记录不完整，至少要写明是不是安打。如果不写，上一垒一律按“失误”统计。
c. 每位球员的准确姓名。姓名中间不能加空格，姓名的字要正确。
d. 每一棒的打点（几个人得分）

球数：有了球数，这个可以统计很多额外的投手和打击员信息。但目前系统还不支持球数相关的统计。
    


记录格式定义
----

每场比赛，整理两个记录文件。每个队伍一个文件。
记录文件第一行是本队的队名，接下来X行是roster。roster以一个空行结束。
接下来是play-by-play的记录部分。


队名
batterID batterName [additional info]
batterID batterName [additional info]
...
batterID batterName [additional info]
        //至少一行空行
P opponentPitcherName // 指定对方投手。接下来直到下一次指定，投手都是这个人

batterID description(打点).方向tag  // 每个play的信息


注释：
- batter ID: integer. 一般为打序，打序相同不能重复。比如替补上场的10棒一般记作102.
- batter name: 不能有空格
- additional info: 一般写位置信息等。允许空格。可以没有这一项，不计入统计。

- Play-by-play description: 支持的格式包括：
  - K: 三振
  - BB：保送
  - G: 地滚封杀一垒
  - Fc: 选杀前卫
  - FO: 接杀
  - 1B/2B/3B:一垒、二垒、三垒安打
  - HR: 本垒打（或本垒跑）
  - E/E2B/E3B/EHR: 因为防守队员失误而上1/2/3/本垒
  - XB+EYB: 本来是X垒安打，但因为防守队员失误而多进了Y个垒。
  - (X): 打点X，写在打击状况后面
  - 其他的未知信息，统一按G统计。

- 方向tag: 可以随意定义，在统计时会整理这个打者各种tag的分布。可以没有这一项。
  - 例如，ssg为游击地滚，rfp为右外平飞，cfa为中外高飞，lfa!为左外强力高飞，pp!为投手平飞强袭。


记录示例
----

（‘//’ 后面的为注释，但系统目前不支持注释）
// file start
数院            // 队名
1  赵靖康 P      // 1棒，姓名，位置
2  张博 FF
3  胡一鸣 3B
4  王竟先 2B
                // roster后面要有空行
P 江红          // 指定对方投手
1 e.3bg         // 1棒赵靖康靠对方失误上垒，方向为三垒地滚。
2 e.ssg         // 2棒张博靠对方失误上垒，方向为游击地滚。
3 2b(2).ssg     // 3棒打出二垒打，下两分，方向为游击地滚
4 1b.2bp        // 4棒打出一垒打，方向为二垒平飞，没下分

P 馒头          // 对方投手更换为“馒头”
1 k             // 1棒赵靖康被三振
2 fc.1bg        // 一垒地滚选杀前卫
3 fo.pa         // 投手高飞接杀
// file end

更多记录示例，详见data/input/ 中的实际文件。

Have Fun!!!

Zifei Shan, Oct 14, 2013