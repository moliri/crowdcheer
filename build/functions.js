/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    $.support.cors=true;
    $('form').submit(function(event){
        # var num = $('#number').val();
        var text = "Haoqi is running today because" + $('#why').val() + ". Send him a cheer!" + "PHONE NUMBER TO RECORD TO";
        var num1 = 4088233859
		var num2 = 8477321145
		var num3 = 7402442738
		var num4 = 5712941193
		var num5 = 5086151289
		var num6 = 7733700051
		var num7 = 8474019260
		
        $.post("https://www.google.com/voice/m/sendsms",{message: text, number: num6}, function(data) {
            $('button').text("Done");
        });
        event.preventDefault();
    });
});


/ google voice number is (209) 382-7693
	(209) 38C-ROWD


  /$.post("https://www.google.com/voice/m/sendsms",{message: text, number: num1, num2, num3, num4, num5, num6, num7}, function(data) {
          $('button').text("Done");