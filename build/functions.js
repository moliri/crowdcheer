/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    $.support.cors=true;
    $('form').submit(function(event){
        
        var str1 = $('#Name').val()
        var str2 = " is running today because "
        var str3 = $('#Why').val()
        var str4 = ". Send a strong and confident motivation cheer! Record your cheer by calling 224-412-4770. Right on!";
        var text = str1.concat(str2,str3, str4);
        
        var num1 = 4088233859
		var num2 = 8477321145
		var num3 = 7402442738
		var num4 = 5712941193
		var num5 = 5086151289
		var num6 = 7733700051
		var num7 = 8474019260
		
		// Sends a text to all teammates asking for motivations 
        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num1}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num2}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num3}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num4}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num5}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num6}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

        $.post("https://crowdcheer.herokuapp.com/send-message",{message: text, number: num7}, function(data) {
            $("CrowdCheerForm").text("Submit");
        });
        event.preventDefault();

    });
});

/ google voice number is (209) 382-7693
	(209) 38C-ROWD
