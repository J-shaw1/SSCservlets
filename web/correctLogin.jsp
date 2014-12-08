<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

	<script type="text/javascript">
	    function showHide(idName) {
		e = document.getElementById(idName);
		//Switch the display property
		if (e.style.display === 'block') {
		    e.style.display = 'none';
		} else {
		    e.style.display = 'block';
		}
	    }
	</script>

        <h1>Welcome 
	    <%  PrintWriter w = new PrintWriter(out);
		w.print(request.getParameter("username"));%>
	</h1>
	<a href="#" onclick="showHide('sendEmail')">Send email</a>
	<div id="sendEmail" style="display: none">
	    <!--Send email code-->
	    <form name="sendEmail" action="NewEmail">
		<fieldset>
		    <legend>Compose email</legend>
		    <table>
			<tr>
			    <td>To:</td> 
			    <td><input type="text" name="to"></td>
			</tr>
			<tr>
			    <td>CC:</td> 
			    <td><input type="text" name="cc"></td>
			</tr>
			<tr>
			    <td>Subject:</td> 
			    <td><input type="text" name="subject"></td>
			</tr>
		    </table>
		    <textarea rows="10" cols="60" name="body"></textarea>
		    <br />
		    <input type="submit" value="Send" width="100px">
		</fieldset>
	    </form>
	</div>

	<br />

	<a href="#" onclick="showHide('search')">Search address book</a>
	<div id="search" style="display: none">
	    <!--Search address book code-->
	    <form name="search" action="SearchContact">
		<fieldset>
		    <legend>Search address book</legend>
		    <table>
			<tr>
			    <td>Forename:</td>
			    <td><input type="text" name="searchForename"></td>
			</tr>
			<tr>
			    <td>Surname:</td>
			    <td><input type="text" name="searchSurname"></td>
			</tr>
		    </table>
		    <input type="submit" value="Search">
		</fieldset>
	    </form>
	</div>

	<br />

	<a href="#" onclick="showHide('addNew')">Add new contact</a>
	<div id="addNew" style="display: none">
	    <!--Add to address book code-->
	    <form name="newContact" action="NewContact">
		<fieldset>
		    <legend>Create new contact</legend>
		    <table>
			<tr>
			    <td>Email address</td>
			    <td><input type="text" name="addEmail"></td>
			</tr>
			<tr>
			    <td>Forename:</td>
			    <td><input type="text" name="addForename"></td>
			</tr>
			<tr>
			    <td>Surname:</td>
			    <td><input type="text" name="addSurname"></td>
			</tr>
		    </table>
		    <input type="submit" value="Add contact">
		</fieldset>
	    </form>
	</div>

    </body>
</html>
