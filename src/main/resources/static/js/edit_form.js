$(document).ready(function () {
    $('#password1, #password2').on('keyup', function () {
        if ($('#password1').val() === $('#password2').val()) {
            $('#confirm_password_message').html('Match').css('color', 'green');
        } else
            $('#confirm_password_message').html('Not Match').css('color', 'red');
    });
})