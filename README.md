# SaveLives
Group project for Cloud Software Design


## Mini Github Guide 
_Note: If you wish to store any output from a git command in a file, do so with `<git_command> > output.txt`, you can replace `output.txt` with a name of your choice. This works in git bash._
### Iterative Development

##### `git status`
List the files that have been modified, as well as files that have not yet been added to the repo.
##### `git commit <filename(s)> -m <commit_message>`
Commit one or more files by listing their file names, and adding a message. Remember to give meaningful commit messages. 
##### `git commit -am <commit_message>`
Commit all files which have been modified. 
##### `git add <file_name(s)`
Add files to the repo, they will still need to be committed!

### Branches and Logs
_Note: The term "checkout" refers to the action of changing all of the files in your repo to match a particular branch or commit._
##### `git checkout <branch_name>`
Checkout a branch, make sure you've committed your changes to your local branch.
##### `git checkout <commit_id>`
Checkout a specific commit. This is a very useful command for when a section of your project was working and is now broken.  
Warning, this command will not save uncommited changes made to your local branch (git usually issues a warning / error about this).
##### `git checkout -b <new_branch_name> <commit_id>`
Create a new branch with the name `<new_branch_name>` using the commit `<commit_id>`.
##### `git diff <commit> <commit>`
This will output the differences between two different commits, very helpful for debugging. You may want to send the output of this command to a different file via the `>` utility (available in git bash).
##### `git log`
Get a list of commits made up until the most recent commit stored on your local branch.
You can then use the commit id returned by this command with `git checkout`.

Outline of a single commit from the command : `git log`.
```
commit <commit_id>
Author: <full_name> <email@domain>
Date:   Weekday Month Day Time Year

    <commit_message>
```

You can then use the `<commit_id>` in conjunction with the `git checkout` command.

### Need help finding help?
##### `git --help`
```
usage: git [--version] [--help] [-C <path>] [-c name=value]
           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]
           [-p | --paginate | --no-pager] [--no-replace-objects] [--bare]
           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]
           <command> [<args>]
```
These are common Git commands used in various situations:

##### Start a working area (see also: git help tutorial)
>   `clone`     Clone a repository into a new directory
>
>   `init`       Create an empty Git repository or reinitialize an existing one


##### Work on the current change (see also: git help everyday)
>   `add`        Add file contents to the index
>
>   `mv`         Move or rename a file, a directory, or a symlink
>
>   `reset`      Reset current HEAD to the specified state
>
>   `rm`         Remove files from the working tree and from the index


##### Examine the history and state (see also: git help revisions)
>   `bisect`     Use binary search to find the commit that introduced a bug
>
>   `grep`       Print lines matching a pattern
>
>   `log`        Show commit logs
>
>   `show`       Show various types of objects
>
>   `status`     Show the working tree status


##### Grow, mark and tweak your common history
>   `branch`     List, create, or delete branches
>
>   `checkout`   Switch branches or restore working tree files
>
>   `commit`     Record changes to the repository
>
>   `diff`       Show changes between commits, commit and working tree, etc
>
>   `merge`      Join two or more development histories together
>
>   `rebase`     Reapply commits on top of another base tip
>
>   `tag`        Create, list, delete or verify a tag object signed with GPG


##### Collaborate (see also: git help workflows)
>   `fetch`      Download objects and refs from another repository
>
>   `pull`       Fetch from and integrate with another repository or a local branch
>
>   `push`       Update remote refs along with associated objects

'git help -a' and 'git help -g' list available subcommands and some
concept guides. See 'git help <command>' or 'git help <concept>'
to read about a specific subcommand or concept.



