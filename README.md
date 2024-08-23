# Remote-job-control-shell




Feature:

   1.execute programs in the remote shell (executable program like command 'ls')
   2.change directory in the remote shell (the built-in command 'cd') using both absolute path and relative path.
      The feature is implemented mainly with a variable in client, holding the filepath of the remote machine.
   3.check if input filepath is valid (using File exist()), invert the change of filepath if invalid
   4.commands are transfered using sockets

