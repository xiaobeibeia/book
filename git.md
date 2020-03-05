Ctrl+l(小写的L) clear 清屏操作

add 暂存区

commit 本地库

#### 1git初始化  git  init

```git


$ git init
Initialized empty Git repository in D:/wechart/.git/

```



```java
祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ ll
total 0

祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ ll -lA
total 4
drwxr-xr-x 1 祝玉洁 197609 0  3月  5 13:43 .git/


```

```

//注意：.git 目录中存放的是本地库相关的子目录和文件，不要删除，也不要胡 乱修改。

祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ ll .git/
total 7
-rw-r--r-- 1 祝玉洁 197609 130  3月  5 13:43 config
-rw-r--r-- 1 祝玉洁 197609  73  3月  5 13:43 description
-rw-r--r-- 1 祝玉洁 197609  23  3月  5 13:43 HEAD
drwxr-xr-x 1 祝玉洁 197609   0  3月  5 13:43 hooks/
drwxr-xr-x 1 祝玉洁 197609   0  3月  5 13:43 info/
drwxr-xr-x 1 祝玉洁 197609   0  3月  5 13:43 objects/
drwxr-xr-x 1 祝玉洁 197609   0  3月  5 13:43 refs/

```

#### 2设置签名 

**形式**

用户名：tom Email 地址：goodMorning@atguigu.com 

 作用：区分不同开发人员的身份

 辨析：这里设置的签名和登录远程库(代码托管中心)的账号、密码没有任何关 系。

 **命令 **

- 项目级别/仓库级别：仅在当前本地库范围内有效

  ​	 git config user.name tom_pro 

  ​	 git config user.email goodMorning_pro@atguigu.com

  ​	 信息保存位置：./.git/config 文件

```java
祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ cat .git/config
[core]
        repositoryformatversion = 0
        filemode = false
        bare = false
        logallrefupdates = true
        symlinks = false
        ignorecase = true
[user]
        name = tom_pro
        email = goodMorning_pro@atguigu.com

```

- 系统用户级别：登录当前操作系统的用户范围

  ​	  git config --globaluser.nametom_glb

  ​	  git config --global goodMorning_pro@atguigu.com 

  ​	 信息保存位置：~/.gitconfig 文件

```java
//~家目录

祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ cd~

祝玉洁@LAPTOP-HSQG8FGL MINGW64 ~
$ PWD
/c/Users/祝玉洁
    
 祝玉洁@LAPTOP-HSQG8FGL MINGW64 ~
$ cat ~/.gitconfig
[user]
        name = tom_glb
        email = goodMorning_pro@atguigu.com


```



#### 3，查询提交的，查看工作区、暂存区状态  git status

```java
祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ git status
On branch master

No commits yet

nothing to commit (create/copy files and use "git add" to track)

```

#### 4.提交的语句， git commit -m "commitmessage" [filename]  

""中是备注信息，不写，则进入vim编辑模式编辑信息

```java
祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ git commit good.text
warning: LF will be replaced by CRLF in good.text.
The file will have its original line endings in your working directory
[master (root-commit) 215f846] My first commit
 1 file changed, 3 insertions(+)
 create mode 100644 good.text

```

#### 5.查询文件

```java
祝玉洁@LAPTOP-HSQG8FGL MINGW64 /d/wechart (master)
$ cat good.text
dfsfsdf
fdsgvvvvfvsdv
ddddddddddddd

```

#### 6 被修改的文件未提交

![image-20200305183843727](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305183843727.png)

##### 二次提交

- 颜色变绿

![image-20200305184314738](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305184314738.png)

 提交 git commit -m "commitmessage" [filename]     将暂存区的内容提交到本地库

#### 7  查看历史记录 

##### git log            多屏显示控制方式： 空格向下翻页 b 向上翻页 q 退出   最完整的

1.git log 命令可以显示所有提交过的版本信息

![image-20200305185100927](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305185100927.png)

##### git log --oneline               简洁的

![image-20200305185612272](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305185612272.png)

##### git reflog                 HEAD@{移动到当前版本需要多少步}

 可以查看所有分支的所有操作记录（包括已经被删除的 commit 记录和 reset 的操作）

例如执行 git reset --hard HEAD~1，退回到上一个版本，用git log则是看不出来被删除的commitid，用git reflog则可以看到被删除的commitid，我们就可以买后悔药，恢复到被删除的那个版本。

![image-20200305185725423](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305185725423.png)

### 8 前进后退

**本质  head指针的移动**

![image-20200305190256723**](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305190256723.png)

#### 8.1 索引值的前进后退

- git reset --hard [局部索引值] 

- git reset --hard a6ace91 

  ![image-20200305190711803](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305190711803.png)

#### 8.2 使用^符号：只能后退

- 一个退一步，两个退两步
- git reset --hard HEAD^
- - 

#### 8.3使用~符号：只能后退 

- git reset --hard HEAD~n 
- 注：表示后退 n 步

#### 8.4  reset 命令的三个参数对比 

-  --soft 参数
  - 仅仅在本地库移动 HEAD 指针
  - ![image-20200305193033991](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305193033991.png)
-  --mixed 参数
  -  在本地库移动 HEAD 指针 
  -  重置暂存区
  - ![image-20200305193243758](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305193243758.png)

-  --hard 参数 
  -  在本地库移动 HEAD 指针 
  - 重置暂存区 
  - 重置工作区

#### 8.5 删除命令

- 删除文件并找回 
  - 前提：删除前，文件存在时的状态提交到了本地库。 
  - 操作：gitreset -- hard [指针位置]  
  - 删除操作已经提交到本地库：指针位置指向历史记录 
  - 删除操作尚未提交到本地库：指针位置使用 HEAD

- 比较文件差异 
  - git diff [文件名] 
  - 将工作区中的文件和暂存区进行比较
  - git diff [本地库中历史版本] [文件名] 
  - 将工作区中的文件和本地库历史记录比较 
  - 不带文件名比较多个文件

#### 9 分支

- 分支是什么？

在版本控制过程中，使用多条线同时推进多个任务。

![image-20200305195544156](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305195544156.png)

- 分支的好处
  - 同时并行推进多个功能开发，提高开发效率 
  - 各个分支在开发过程中，如果某一个分支开发失败，不会对其他分支有任 何影响。失败的分支删除重新开始即可。

- 创建分支

  -  创建分支 git branch [分支名]

  - 查看分支 git branch -v

  -  切换分支 git checkout [分支名] 

    ![image-20200305202454321](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305202454321.png)

  - 合并分支 

    - 第一步：切换到接受修改的分支（被合并，增加新内容）上 git checkout [被合并分支名]
    -  第二步：执行 merge 命令 git merge [有新内容分支名] 

  -  解决冲突

    -  冲突的表现

      ![image-20200305203146462](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305203146462.png)

    - 冲突的解决

      -  第一步：编辑文件，删除特殊符号 
      - 第二步：把文件修改到满意的程度，保存退出 
      - 第三步：git add [文件名] 
      - 第四步：git commit -m "日志信息" 
        - 注意：此时 commit 一定不能带具体文件名

#### 10 git 存网页地址

#### 创建远程库地址别名

####  git remote -v 查看当前所有远程地址别名 

####  git remote add [别名] [远程地址]

![image-20200305211948278](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305211948278.png)



#### 11 推送  git push [别名] [分支名]

![image-20200305214545488](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305214545488.png)

#### 12 克隆 

- 命令   git clone [远程地址]

  ![image-20200305214523188](C:\Users\祝玉洁\AppData\Roaming\Typora\typora-user-images\image-20200305214523188.png)

-  效果 

  - 完整的把远程库下载到本地

  - 创建 origin 远程地址别名 

  - 初始化本地库