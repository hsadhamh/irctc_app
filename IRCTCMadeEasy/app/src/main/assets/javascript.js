var ticketDetails = new Array();
var timerID;

var timercounter=0;

function scrollToGivenId(wrapper){
    console.log('Scrolling to ID ' + wrapper);
    if(wrapper !== null && wrapper.offset() !== null){
        $('html, body').animate({
            scrollTop: wrapper.offset().top
        }, 1000);
    }
}

function isUserLoggedIn(){
    console.log('is Logged in');
    var elements = $("a[title='Log Out']").get();
    return !!(elements !== null && elements.length > 0);
}

function isPlanMyTravelAvailable(){
console.log('isPlanMyTravelAvailable - check for it');
    var elements = $("form[id='jpform']").get();
    return !!(elements !== null && elements.length > 0);
}

function isLoginPage() {
    console.log('isLoginPage - check for it');
    var elements = $("form[id='loginFormId']").get();
    return !!(elements !== null && elements.length > 0);
}

function isTrainsListed() {
    var elements = $("table[id='avlAndFareForm:trainbtwnstns']").get();
    if(elements !== null && elements.length > 0) {
        var entered_board_station = $("input[id='jpform:fromStation']").val().trim();
        if(entered_board_station == ticketDetails['source']) {
            var entered_dest_station = $("input[id='jpform:toStation']").val().trim();
            if(entered_dest_station == ticketDetails['destination']) {
                var dd = ticketDetails['datejourney'].split("-");
                dd[0] = ('0' + dd[0]).slice(-2);
                dd[1] = ('0' + dd[1]).slice(-2);
                var d = new Date(dd[2] + "-" + dd[1] + "-" + dd[0]);
                var curr_date = ('0' + d.getDate()).slice(-2);
                var curr_year = d.getFullYear();
                var full_date = curr_date + "-" + dd[1] + "-" + curr_year;
                var entered_travel_date = $("input[name='jpform:journeyDateInputDate']").val().trim();
                console.log('isTrainsListed - check for date [' + full_date + '] : ['+ entered_travel_date +'] ');
                if(entered_travel_date == full_date) {
                    return true;
                }
            }
        }
    }
    return false;
}

function loginToForm() {
    if(ticketDetails['username'])
    {
        console.log(' login form ');
        $("input[id='usernameId']").val(ticketDetails['username']);
        $("input[name='j_password']").val(ticketDetails['password']);
        $("input[name='j_captcha']").focus();

        $("input[name='j_captcha']").css("width", "150%");
        $("input[name='j_captcha']").css("height", "100%");

        $("#cimage").css("width","250%");
        $("#cimage").css("height","150%");

        scrollToGivenId($("input[id='usernameId']"));
    }
}

function isTravelDetailsError() {
    if($('span[id="jpform:journeyDateErr"]').children().length > 0) {
        return true;
    }

    if($('span[id="jpform:toStationErr"]').children().length > 0) {
        return true;
    }

    return $('span[id="jpform:fromStationErr"]').children().length > 0;

}

function planMyTravel() {
    if(isTravelDetailsError()) {
        return;
    }
    /*
     *   RESET is not working. Need to check.
     *   $("input[value='Reset']").click();
     */
    $("input[name='jpform:fromStation']").val(ticketDetails['source']);
    $("input[name='jpform:toStation']").val(ticketDetails['destination']);
    var dd = ticketDetails['datejourney'].split("-");
    dd[0] = ('0' + dd[0]).slice(-2);
    dd[1] = ('0' + dd[1]).slice(-2);
    var d = new Date(dd[2] + "-" + dd[1] + "-" + dd[0]);
    var curr_date = d.getDate();
    var curr_year = d.getFullYear();
    $("input[name='jpform:journeyDateInputDate']").val(curr_date + "-" + dd[1] + "-" + curr_year);
    $("select[name='ticketType']").val('eticket');
    $("#jpform ").find("input[type='submit']").click();
    /*Need to verify if it works*/
}

function bookNow(trainNo, berthclass, travelQuota) {
    timercounter++;
    if($("div[id ='tabcontent']").length === 0){
        return;
    }
    clearInterval(timerID);
    var quota_txt = getQuotaText(travelQuota);

    var tabHeader = "" + trainNo + "-" + berthclass + "-" + quota_txt;
    var tabHeadCount = 0;
    $("ul[id='tabul']").find('li').each(function () {
        if ($(this).text().indexOf(tabHeader) >= 0)
        {
            return false;
        }
        tabHeadCount++;
    });
    /*  check for error now. */
    if(IsLoadAvailabilityError()) {
        return;
    }

    var dd = ticketDetails['datejourney'].split("-");
    dd[0] = (dd[0]).slice(-2);
    dd[0] = dd[0].replace(/^0+/, '');
    dd[1] = (dd[1]).slice(-2);
    dd[1] = dd[1].replace(/^0+/, '');
    var full_date = dd[0] + "-" + dd[1] + "-" + dd[2];

    var tabContentDiv = $("div[id='tabcontent']");
    if(tabContentDiv !== null && tabContentDiv.find('.container-div:eq('+tabHeadCount+')').find("table:eq(0)").length != 0)
    {
        var countTicketAvail = 0;
        /*Place where book now is found.*/
        tabContentDiv.find('.container-div:eq('+tabHeadCount+') table:eq(0) tr:eq(0) td ')
            .each(function () {
                if($(this).text() == full_date)
                {
                    return false;
                }
                countTicketAvail++;
            });
        tabContentDiv.find('.container-div:eq('+tabHeadCount+') table:eq(0) tr:eq(1) td:eq('+ (countTicketAvail)+')')
            .find("a[id='" + tabHeader + "-"+ (countTicketAvail - 1) +"']").each(function() {
            if($(this).text() == "Book Now") {
                $(this)[0].click();
            }
        });
    }
}

function getQuotaText(travelQuota) {
    var quota_txt = 'GN';
    if(travelQuota == 'GENERAL')
        quota_txt = 'GN';
    else if(travelQuota == 'PREMIUM TATKAL')
        quota_txt = 'PT';
    else if(travelQuota == 'PHYSICALLY HANDICAP')
        quota_txt = 'HP';
    else if(travelQuota == 'LADIES')
        quota_txt = 'LD';
    else if(travelQuota == 'TATKAL')
        quota_txt = 'CK';
    return quota_txt;
}

function IsLoadAvailabilityError() {
    if($("div[id='tabcontent']").find('.container-div:eq(tabHeadCount)').find("#errorpanelid1").length != 0) {
        console.log('Retirn on error');
        return true;
    }
    return false;
}

function isPaymentPage() {
    console.log(' isPaymentPage ');
    var elements = $("div[id='paymentMsgBox']").get();
    if(elements != null && elements.length > 0) {
        return true;
    } else {
        elements = $("td[id='NETBANKING']").get();
        if(elements != null && elements.length > 0) {
            return true;
        }
    }
    return false;
}

function isPassengerListPage() {
    var elements = $("table[id='addPassengerForm:psdetail']").get();
    return !!(elements != null && elements.length > 0);

}

function fillPassengerInformation() {
    $("#addPassengerForm").find("td").each(function() {
        if($(this).text().indexOf("Journey date :") == 0) {
            var str0 = $(this).next().text().trim();
            var dd = ticketDetails['datejourney'].split("-");
            dd[0] = ('0' + dd[0]).slice(-2);
            dd[1] = ('0' + dd[1]).slice(-2);
            var d = new Date(dd[2] + "-" + dd[1] + "-" + dd[0]);
            var m_names = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var curr_date = ('0' + d.getDate()).slice(-2);
            var curr_month = d.getMonth();
            var curr_year = d.getFullYear();
            var str1 = curr_date + "-" + m_names[curr_month] + "-" + curr_year;
            if (str1 == str0) {
                var boarding_middle_station = ticketDetails["boarding"];
                if(boarding_middle_station) {
                    var arrBoardingSplit = boarding_middle_station.split("-");
                    if(arrBoardingSplit.length == 2) {
                        $("select[name='addPassengerForm:boardingStation']").val(arrBoardingSplit[1].trim());
                    }
                }

                var noOfPassengers = 6;
                var noOfChilds = 2;
                if(ticketDetails['quota'])
                {
                    if(ticketDetails['quota'] == 'GENERAL') {
                        noOfPassengers = 6;
                        noOfChilds = 2;
                    }
                    else if(ticketDetails['quota'] == 'TATKAL') {
                        noOfPassengers = 4;
                        noOfChilds = 2;
                    }
                }

                for(i=0; i < noOfPassengers; i++) {
                    console.log("fillPassengerInformation - pass details " + i);
                    if (ticketDetails['passengerinfo'][i]) {
                        var passDetails = ticketDetails['passengerinfo'][i];
                        var rowElement = $('tr[id="addPassengerForm:psdetail:' + i + '"]');
                        console.log("fillPassengerInformation - pass details " + i + " : adult.");
                        $(rowElement).find("input.psgn-name").val(passDetails.name);
                        console.log("fillPassengerInformation - pass details " + passDetails.name);
                        $(rowElement).find("input.psgn-age").val(passDetails.age);
                        console.log("fillPassengerInformation - pass details " + passDetails.age);
                        var strGen = passDetails.gender;
                        strGen = (strGen == 'Male')? 'M' : 'F';
                        $(rowElement).find("select.psgn-gender").val(strGen);
                        console.log("fillPassengerInformation - pass details " + strGen);
                        $(rowElement).find("select.psgn-berth-choice").val(getBerthValue(passDetails.berth));
                        console.log("fillPassengerInformation - pass details " + getBerthValue(passDetails.berth));
                        /*if (!(passDetails.nationality == 'indian')) {
                            $("select[name='addPassengerForm:psdetail:" + i + ":idCardType']").val(passDetails.idtype);
                            $("input[name='addPassengerForm:psdetail:" + i + ":idCardNumber']").val(passDetails.idno);
                        }*/
                        /*if (passDetails.food == 'Veg') {
                            $("select[name='addPassengerForm:psdetail:" + i + ":foodChoice']").val("V");
                        } else {
                            $("select[name='addPassengerForm:psdetail:" + i + ":foodChoice']").val("N");
                        }*/
                        if (passDetails.age >= 5 && passDetails.age <= 11) {
                            $("input[name='addPassengerForm:psdetail:" + i + ":childBerthOpt']").removeAttr("disabled");
                        }
                        if (passDetails.senior == 'true') {
                            $("input[name='addPassengerForm:psdetail:" + i + ":concessionOpt']").prop('checked', true);
                        }
                    }
                    else {
                        $("input[name='addPassengerForm:psdetail:"+j+":psgnName']").val("");
                        $("input[name='addPassengerForm:psdetail:"+j+":psgnAge']").val("");
                        $("select[name='addPassengerForm:psdetail:"+j+":psgnGender']").val(" ");
                        $("select[name='addPassengerForm:psdetail:"+j+":berthChoice']").val("  ");
                        $("select[name='addPassengerForm:psdetail:"+j+":idCardType']").val("NULL_IDCARD");
                        $("input[name='addPassengerForm:psdetail:"+j+":idCardNumber']").val("");
                        $("select[name='addPassengerForm:psdetail:"+j+":foodChoice']").val(" ");
                        $("input[name='addPassengerForm:psdetail:"+j+":concessionOpt']").prop("disabled", "disabled");
                        $("input[name='addPassengerForm:psdetail:"+j+":concessionOpt']").prop('checked', false);
                    }
                }

                for(i=0; i < noOfChilds; i++){
                    if (ticketDetails['childinfo'][i]) {
                        var passDetails = ticketDetails['childinfo'][i];
                        console.log("fillPassengerInformation - pass details " + i + " : child. ");
                        $("input[name='addPassengerForm:childInfoTable:"+ i +":infantName']").val(passDetails.name);
                        console.log("fillPassengerInformation - pass details " + passDetails.name);
                        $("select[name='addPassengerForm:childInfoTable:"+ i +":infantAge']").val(passDetails.age);
                        console.log("fillPassengerInformation - pass details " + passDetails.age);
                        var strGen = passDetails.gender;
                        strGen = (strGen == 'Male')? 'M' : 'F';
                        $("select[name='addPassengerForm:childInfoTable:"+ i +":infantGender']").val(strGen);
                        console.log("fillPassengerInformation - pass details " + passDetails.gender);
                    }
                }

                var mobNum = ticketDetails['mobile'];
                if(mobNum) {
                    $("input[id='addPassengerForm:mobileNo']").val(mobNum);
                }

                if (ticketDetails['autoupgrade']=='true')
                    $("input[id='addPassengerForm:autoUpgrade']").prop('checked', 'checked');

                if (ticketDetails['bookconfirm']=='false')
                    $("input[id='addPassengerForm:onlyConfirmBerths']").prop('checked', 'checked');
                $("input[name='addPassengerForm:bookingCond'][value='"+ticketDetails['book-id-cond']+"']").each(function() {
                    $(this)[0].click();
                });

                if (ticketDetails['prefercoach']=='false') {
                    $("input[id='addPassengerForm:prefCoachOpt']").click();
                    $("input[id='addPassengerForm:coachID']").val(ticketDetails['coachid']);
                }

                if ($("input[id='#j_captcha']").length > 0) {
                    $("input[id='#j_captcha']").focus();

                    $("input[id='#j_captcha']").css("width", "150%");
                    $("input[id='#j_captcha']").css("width", "125%");

                    scrollToGivenId($("input[id='#j_captcha']"));
                } else {
                    if($("div[id='nlpCaptchaContainer']").length > 0) {
                        $("div[id='nlpCaptchaContainer']").css("width", "250%");
                        $("div[id='nlpCaptchaContainer']").css("width", "250%");
                        if ($("#nlpCaptchaImg").length > 0) {
                            $("input[id='#nlpAnswer']").focus();

                            scrollToGivenId($("div[id='nlpCaptchaContainer']"));
                        }
                    }
                }
            } else {
                $("input[id='addPassengerForm:replan']").click();
                $.prompt("Ticket not available on selected date");
            }
        }
    });
}

function fillPaymentDetails() {
    console.log(' fillPaymentDetails ');

    var alertVal = false;
    var availStatus = "";
    $("div[id*='jpBook:']").find("td").each(function(){
        var curchild = $(this).children("b");
        if (curchild) {
            var curchild2 = curchild.find("label");
            if(curchild2.length > 0){
                curchild2.each(function(){
                    if($(this).text().trim().indexOf("Availability :") == 0) {
                        var str0 = $(this).parent().parent().next().text().trim().toLowerCase();
                        availStatus = str0;
                        if(str0.indexOf('available') < 0 && str0.indexOf('curr_avbl') < 0) {
                            alertVal = true;
                        }
                    }
                });
            }
        }
    });

    console.log(' fillPaymentDetails : '+ ticketDetails['paymentmode'] +' id : ' + ticketDetails['paymentmodeid']);

    $("td[id='"+ticketDetails['paymentmode']+"']").click();
    if(ticketDetails['paymentmode'] == 'NETBANKING') {
        $("input[name='NETBANKING'][value='"+ticketDetails['paymentmodeid']+"']").each(function(){
            $(this)[0].click();
        });
    }
    if(ticketDetails['paymentmode'] == 'CREDIT_CARD') {
        $("input[name='CREDIT_CARD'][value='"+ticketDetails['paymentmodeid']+"']").each(function(){
            $(this)[0].click();
        });
    }
    if(ticketDetails['paymentmode'] == 'DEBIT_CARD') {
        $("input[name='DEBIT_CARD'][value='"+ticketDetails['paymentmodeid']+"']").each(function(){
            $(this)[0].click();
        });
    }
    if(ticketDetails['paymentmode'] == 'CASH_CARD') {
        $("input[name='CASH_CARD'][value='"+ticketDetails['paymentmodeid']+"']").each(function(){
            $(this)[0].click();
        });
    }
    if(ticketDetails['paymentmode'] == 'IRCTC_PREPAID') {
        $("input[name='IRCTC_PREPAID'][value='"+ticketDetails['paymentmodeid']+"']").each(function(){
            $(this)[0].click();
        });
    }

    if ((ticketDetails['paymentmode'] == "CREDIT_CARD" || ticketDetails['paymentmode'] == "IRCTC_PREPAID") &&
        (ticketDetails['paymentmodeid'] == 21 || ticketDetails['paymentmodeid'] == 59))
    {
        $("div[id='card_div_id']").css("display","");
        $("select[id='card_type_id']").val(ticketDetails['cardtype']);

        var ccNum = ticketDetails['cardnovalue'];
        if(ccNum) {
            $("input[id='card_no_id']").val(ccNum);
        }

        $("select[id='card_expiry_mon_id']").val(ticketDetails['expirymon']);
        $("input[id='card_expiry_year_id']").val(ticketDetails['expiryyear']);
        $("input[id='card_name_id']").val(ticketDetails['name-card']);
        if(ticketDetails['cardcvv']) {
            $("input[id='cvv_no_id']").val(ticketDetails['cardcvv']);
            $("input[id='captcha_txt']").focus();
        } else {
            $("input[id='cvv_no_id']").focus();
        }
        scrollToGivenId($("input[id='cvv_no_id']"));
        if(alertVal){
            $.prompt('Ticket is in waiting list.');
        }
    }
    else
    {
        $("input[id='validate']").click();
    }

    $("input[name='j_captcha']").keypress(function(e){
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if(keycode == '13') {
            e.preventDefault();
            $("input[id='validate']").click();
            return false;
        }
    });
}

function getBerthValue(berth_txt){
    var quota_txt = ' ';
    if(berth_txt == 'Lower')
        quota_txt = 'LB';
    else if(berth_txt == 'Middle')
        quota_txt = 'MB';
    else if(berth_txt == 'Upper')
        quota_txt = 'UB';
    else if(berth_txt == 'Side Upper')
        quota_txt = 'SU';
    else if(berth_txt == 'Side Lower')
        quota_txt = 'SL';
    else if(berth_txt == 'Window Seat')
            quota_txt = 'WS';
    return quota_txt;
}

function doAutomation() {
    ticketDetails = JSON.parse(jsonString);
    if(!isUserLoggedIn())
    {
        if (isLoginPage()) {
            loginToForm();
            $("img[id='cimage']").width('125%');
        } else {
            window.location.href = "https://www.irctc.co.in/eticketing/loginHome.jsf";
        }
    }
    else if(isTrainsListed()) {
    	scrollToGivenId($("table[id='avlAndFareForm:trainbtwnstns']"));
var frame = document.getElementById("randomid");
 frame.parentNode.removeChild(frame);
        var quota = ticketDetails["quota"];
        var quotaRadioButtons = $("input[name='quota']").get();
        var quota_txt = getQuotaText(quota);
        var eachRadioElement;
        for (var i = quotaRadioButtons.length - 1; i >= 0; i--) {
            eachRadioElement = quotaRadioButtons[i];
            if (eachRadioElement.value == quota_txt) {
                eachRadioElement.checked = true;
            }
        }
        var trainNo = ticketDetails["trainno"].split(":")[0].trim();
        var berthclass = ticketDetails["bclass"];
        var elements = $("table[id='avlAndFareForm:trainbtwnstns'] > tbody > tr[id*='avlAndFareForm:trainbtwnstns']").get();
        if(elements != null && elements.length > 0) {
            var trainFound = false;
            var berthFound = false;
            $("table[id='avlAndFareForm:trainbtwnstns'] > tbody > tr[id*='avlAndFareForm:trainbtwnstns']").each(function() {
                if($(this).find('td:first-child > a').text() == trainNo) {
                    trainFound = true;
                    $(this).find('td:last-child > a').each(function() {
                        if($(this).text() == berthclass) {
                            $(this)[0].click();
                            berthFound = true;
                            var dd = ticketDetails['datejourney'].split("-");
                            dd[0] = ('0' + dd[0]).slice(-2);
                            dd[1] = ('0' + dd[1]).slice(-2);
                            var d = new Date(dd[2] + "-" + dd[1] + "-" + dd[0]);
                            var curr_date = d.getDate();
                            var curr_month = d.getMonth();
                            var curr_year = d.getFullYear();
                            var travelDate = curr_date + "-" + dd[1] + "-" + curr_year;
                            var fromStation = ticketDetails['boarding'].split('-');
                            var fromStationCode = fromStation[fromStation.length - 1].trim();
                            var toStation = ticketDetails['destination'].split('-');
                            var toStationCode = toStation[toStation.length - 1].trim();
                            timerID = setInterval(function () {
                                bookNow(trainNo, berthclass, quota);
                            }, 1000);
                        }
                    });
                }
            });

            if (!(trainFound)) {
                $.prompt('The selected train is not available on the selected date. Please check if the data entered is correct.');
            } else if (!(berthFound)) {
                $.prompt('The selected berth \'' + berthclass + '\' is not available in the chosen train. Please check if the data entered is correct.');
            }
        }
    }
    else if(isPlanMyTravelAvailable()) {
    	scrollToGivenId($("input[name='jpform:fromStation']"));
        planMyTravel();
    }
    else if(isPaymentPage()) {
    	scrollToGivenId($("div[id='paymentMsgBox']"));
        fillPaymentDetails();
        $("img[id='cimage']").width('125%');
    }
    else if(isPassengerListPage()) {
    	scrollToGivenId($("table[id='addPassengerForm:psdetail']"));
        fillPassengerInformation();
        $("img[id='bkg_captcha']").width('125%');
    }
}

doAutomation();