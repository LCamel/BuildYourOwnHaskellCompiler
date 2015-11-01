Env 的作法想了很久沒想通.

像這個例子 (λy ( (λx (λy x) ) y) ) 用 Env 做如果要印出 normal form 來, 也是得換變數名稱.

既然這樣, 就來看 De Bruijn 的表示式.

```
      lam
    x    app
        x   lam
           y   x
```
變成
```
      lam
         app
        1  lam
              2
```
大概吧.

http://www.mpi-sws.org/~umut/classes/CMSC321-Fall05/lectures/lec6.pdf

```
    ....
    ....
    app
lam     B
   A
```
取代來了.

要把 A 裡面的, 指到 lam 的那些 var, 通通換成 B.
而 B 裡面指到 B 外面的, 還是要指到原來的地方.

很幸運的, 這個取代似乎只需要這個 app 以下的資訊, 不需要上面傳下來的其他東西. (?)

B 裡面指到 B 外面的, 會隨著帶入到 A 中的深度而有所差異. 被帶入的越深, 往上指的距離越長.

實作了 De Bruijn 的化簡, 覺得和有變數名字的比起來觀念更簡單, "連結" 感也更強.
