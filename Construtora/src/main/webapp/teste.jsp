<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Enviar E-Mail</title>
</head>
<body>

    <h1>Enviar E-Mail</h1>
    <form action="sendEmail.do" method="post">
        <table>
            <tr>
                <td>De</td>
                <td><input type="text" name="from" /></td>
            </tr>
            <tr>
            <tr>
                <td>Para</td>
                <td><input type="text" name="to" /></td>
            </tr>
            <tr>
                <td>Assunto</td>
                <td><input type="text" name="subject" /></td>
            </tr>
            <tr>
                <td>Mensagem</td>
                <td><textarea cols="25" rows="8" name="message"></textarea></td>
            </tr>
        </table>
        <br /> <input type="submit" value="Enviar" />
    </form>
</body>
</html>