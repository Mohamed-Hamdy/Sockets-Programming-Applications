# java-problems
<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#Sockets Programming">Sockets Programming Projects</a>
      <ul>
        <li><a href="#problem_1">Online-Text-Editor</a></li>
      </ul>
      <ul>
        <li><a href="#problem_2">Http-Server-Sockets</a></li>
      </ul>
      <ul>
        <li><a href="#problem_3">Email Server</a></li>
      </ul>
     </li>
  </ol>
</details>




### problem_1
<h3>Online Text Editor</h3>

problem Decription:
Java Socket programming project that allow users to use same text editor and share any edit concurrently if they connect to same server.
* One class for server, and one for client. The client will be run multiple of times.  
* Any client can request from the server to share some text with some other clients. 
* The server sends this text to the intended clients.    
* The text appears to each client on its console
* Any client can change in the shared text and this change will be send to server that will send it to all other clients sharing the same text. And then appears to their consoles. 
* Changing in same text concurrently will be considered as an extra function. 
* Multiple texts can be shared concurrently between the same clients or between different clients. 

<h4>How to Run</h4>
* 1- First Run the Server file 
* 2- Run the Client file 
* 3- the program will show the text editor in client side 

<h4>Project Gui</h4>
<img src="https://github.com/Mohamed-Hamdy/java-problems/blob/main/Sockets%20Programming/Online%20Text%20Editor/project%20gui.png" alt="Project Gui">

<h4><a href="https://github.com/Mohamed-Hamdy/java-problems/tree/main/Sockets%20Programming/Online%20Text%20Editor">Project Code</a></h3>
<hr>


### problem_2
<h3>Http-Server-Sockets</h3>

problem Decription:
Java Socket programming project that allow users to use same text editor and share any edit concurrently if they connect to same server.
* One class for server, and one for client. The client will be run multiple of times.  
* Any client can request from the server to share some text with some other clients. 
* The server sends this text to the intended clients.    
* The text appears to each client on its console
* Any client can change in the shared text and this change will be send to server that will send it to all other clients sharing the same text. And then appears to their consoles. 
* Changing in same text concurrently will be considered as an extra function. 
* Multiple texts can be shared concurrently between the same clients or between different clients. 

<h4>How to Run</h4>
* First Run the Server file 
* Run the Client file 
* open your browser 
* Enter http://localhost:8080 in url 
* the veggia template will display on server side 

<h4>Project Gui</h4>
<img src="https://github.com/Mohamed-Hamdy/java-problems/blob/main/Sockets%20Programming/Http-Server-Sockets/project%20gui.png" alt="Project Gui">

<h4><a href="https://github.com/Mohamed-Hamdy/java-problems/tree/main/Sockets%20Programming/Http-Server-Sockets">Project Code</a></h3>
<hr>


### problem_3
<h3>Email Server</h3>

problem Decription:
Java Socket programming project that allow users to use same text editor and share any edit concurrently if they connect to same server.
* Any client can send an email to multiple connected clients
* The email content which is (From, To, Subject, Body) is sent to the server. 
* The server forwards the email content to all the intended clients.    
* Server saves all the mails for all connected clients in its memory
* Each connected client can request from the server to show all its emails. 
* Sending to offline clients will be considered as an extra function. 
* Mark email as spam, and block any client will be considered as extra functions.  

<h4>How to Run</h4>
* First Run the Server file 
* Run the Client file 
* run EmailClientcomposeMessage

<h4>Project Gui</h4>
<img src="https://github.com/Mohamed-Hamdy/java-problems/blob/main/Sockets%20Programming/Email-Server/1.png" alt="Project Gui">

<h4><a href="https://github.com/Mohamed-Hamdy/java-problems/tree/main/Sockets%20Programming/Email-Server">Project Code</a></h3>


<hr>