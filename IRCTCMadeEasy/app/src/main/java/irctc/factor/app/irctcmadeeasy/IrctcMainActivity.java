package irctc.factor.app.irctcmadeeasy;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import irctc.factor.app.irctcmadeeasy.WebView.LxWebView;

public class IrctcMainActivity extends AppCompatActivity {

    String mJsForTicket = "\n" +
            "javascript:\n" +
            "    var ticketDetails = new Array();\n" +
            "var timerID;\n" +
            "var jsonString =  '{ \"username\":\"hssbs\", \"password\":\"hssbs1992\", \"source\":\"COIMBATORE JN - CBE\", ' +\n" +
            "    '\"destination\":\"KSR BENGALURU - SBC\", \"boarding\":\"ERODE JN - ED\", \"date-journey\":\"10-07-2016\", ' +\n" +
            "    '\"train-no\":\"12678\", \"class\":\"2S\", \"Quota\":\"GENERAL\", ' +\n" +
            "    '\"child-passenger-info\":[{\"name\":\"Hussain\", \"age\":\"2\", \"gender\":\"M\"}],' +\n" +
            "    '\"passenger-info\":[ ' +\n" +
            "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"21\", \"gender\":\"M\", \"berth\":\"UB\", \"nationality\":\"indian\", ' +\n" +
            "    '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' +\n" +
            "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", \"ID-card\":\"\", ' +\n" +
            "    '\"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" }, ' +\n" +
            "    '{ \"name\":\"Sadham Hussain H\", \"age\":\"25\", \"gender\":\"M\", \"berth\":\"LB\", \"nationality\":\"indian\", ' +\n" +
            "    '\"ID-card\":\"\", \"ID-Card-No\":\"\", \"type\":\"adult\", \"senior\":\"false\" } ], ' +\n" +
            "    '\"Auto-Upgrade\":\"false\", \"book-confirm\":\"false\", \"book-id-cond\":\"2\", \"preferred-coach\":\"false\", \"coachID\":\"S7\", ' +\n" +
            "    '\"mobile\":\"9500454034\", \"payment-mode\":\"CREDIT_CARD\", \"payment-mode-id\":\"21\", \"card-no-value\":\"5241465278458104\", \"card-type\":\"MC\", ' +\n" +
            "    '\"expiry-mon\":\"02\", \"expiry-year\":\"2018\", \"Card-CVV\":\"374\", \"name-card\":\"Sadham Hussain H\" } ';\n" +
            "var timercounter=0;\n" +
            "\n" +
            "function isUserLoggedIn(){\n" +
            "    var elements = $(\"a[title='Log Out']\").get();\n" +
            "    return !!(elements !== null && elements.length > 0);\n" +
            "}\n" +
            "function isPlanMyTravelAvailable(){\n" +
            "    var elements = $(\"form[id='jpform']\").get();\n" +
            "    return !!(elements !== null && elements.length > 0);\n" +
            "}\n" +
            "function isLoginPage() {\n" +
            "    var elements = $(\"form[id='loginFormId']\").get();\n" +
            "    return !!(elements !== null && elements.length > 0);\n" +
            "}\n" +
            "function isTrainsListed() {\n" +
            "    var elements = $(\"table[id='avlAndFareForm:trainbtwnstns']\").get();\n" +
            "    if(elements !== null && elements.length > 0) {\n" +
            "        var entered_board_station = $(\"input[id='jpform:fromStation']\").val().trim();\n" +
            "        if(entered_board_station == ticketDetails['source']) {\n" +
            "            var entered_dest_station = $(\"input[id='jpform:toStation']\").val().trim();\n" +
            "            if(entered_dest_station == ticketDetails['destination']) {\n" +
            "                var dd = ticketDetails['date-journey'].split(\"-\");\n" +
            "                dd[0] = ('0' + dd[0]).slice(-2);\n" +
            "                dd[1] = ('0' + dd[1]).slice(-2);\n" +
            "                var d = new Date(dd[2] + \"-\" + dd[1] + \"-\" + dd[0]);\n" +
            "                var curr_date = ('0' + d.getDate()).slice(-2);\n" +
            "                var curr_year = d.getFullYear();\n" +
            "                var full_date = curr_date + \"-\" + dd[1] + \"-\" + curr_year;\n" +
            "                var entered_travel_date = $(\"input[name='jpform:journeyDateInputDate']\").val().trim();\n" +
            "                if(entered_travel_date == full_date) {\n" +
            "                    return true;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    return false;\n" +
            "}\n" +
            "function loginToForm() {\n" +
            "    if(ticketDetails['username'])\n" +
            "    {\n" +
            "        $(\"input[id='usernameId']\").val(ticketDetails['username']);\n" +
            "        $(\"input[name='j_password']\").val(ticketDetails['password']);\n" +
            "        $(\"input[name='j_captcha']\").focus();\n" +
            "\n" +
            "        $(\"#cimage\").css(\"width\",\"300%\");\n" +
            "        $(\"#cimage\").css(\"height\",\"150%\");\n" +
            "        $(\"input[name='j_captcha']\").css(\"height\",\"150%\");\n" +

            "        $(\"div[id='grid_6']\").css(\"display\",\"none\");\n" +
            "    }\n" +
            "}\n" +
            "function isTravelDetailsError() {\n" +
            "    if($('span[id=\"jpform:journeyDateErr\"]').children().length > 0) {\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    if($('span[id=\"jpform:toStationErr\"]').children().length > 0) {\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    return $('span[id=\"jpform:fromStationErr\"]').children().length > 0;\n" +
            "\n" +
            "}\n" +
            "function planMyTravel() {\n" +
            "    if(isTravelDetailsError()) {\n" +
            "        return;\n" +
            "    }\n" +
            "    /*\n" +
            "     *   RESET is not working. Need to check.\n" +
            "     *   $(\"input[value='Reset']\").click();\n" +
            "     */\n" +
            "    $(\"input[name='jpform:fromStation']\").val(ticketDetails['source']);\n" +
            "    $(\"input[name='jpform:toStation']\").val(ticketDetails['destination']);\n" +
            "    var dd = ticketDetails['date-journey'].split(\"-\");\n" +
            "    dd[0] = ('0' + dd[0]).slice(-2);\n" +
            "    dd[1] = ('0' + dd[1]).slice(-2);\n" +
            "    var d = new Date(dd[2] + \"-\" + dd[1] + \"-\" + dd[0]);\n" +
            "    var curr_date = d.getDate();\n" +
            "    var curr_year = d.getFullYear();\n" +
            "    $(\"input[name='jpform:journeyDateInputDate']\").val(curr_date + \"-\" + dd[1] + \"-\" + curr_year);\n" +
            "    $(\"select[name='ticketType']\").val('eticket');\n" +
            "    $(\"#jpform \").find(\"input[type='submit']\").click();\n" +
            "    /*Need to verify if it works*/\n" +
            "}\n" +
            "function bookNow(trainNo, berthclass, travelQuota) {\n" +
            "    timercounter++;\n" +
            "    if($(\"div[id ='tabcontent']\").length === 0)\n" +
            "    {\n" +
            "        return;\n" +
            "    }\n" +
            "    clearInterval(timerID);\n" +
            "    var quota_txt = getQuotaText(travelQuota);\n" +
            "\n" +
            "    var tabHeader = \"\" + trainNo + \"-\" + berthclass + \"-\" + quota_txt;\n" +
            "    var tabHeadCount = 0;\n" +
            "    $(\"ul[id='tabul']\").find('li').each(function () {\n" +
            "        if ($(this).text().indexOf(tabHeader) >= 0)\n" +
            "        {\n" +
            "            return false;\n" +
            "        }\n" +
            "        tabHeadCount++;\n" +
            "    });\n" +
            "    /*  check for error now. */\n" +
            "    if(IsLoadAvailabilityError()) {\n" +
            "        return;\n" +
            "    }\n" +
            "\n" +
            "    var dd = ticketDetails['date-journey'].split(\"-\");\n" +
            "    dd[0] = (dd[0]).slice(-2);\n" +
            "    dd[0] = dd[0].replace(/^0+/, '');\n" +
            "    dd[1] = (dd[1]).slice(-2);\n" +
            "    dd[1] = dd[1].replace(/^0+/, '');\n" +
            "    var full_date = dd[0] + \"-\" + dd[1] + \"-\" + dd[2];\n" +
            "\n" +
            "    var tabContentDiv = $(\"div[id='tabcontent']\");\n" +
            "    if(tabContentDiv !== null && tabContentDiv.find('.container-div:eq('+tabHeadCount+')').find(\"table:eq(0)\").length != 0)\n" +
            "    {\n" +
            "        var countTicketAvail = 0;\n" +
            "        /*Place where book now is found.*/\n" +
            "        tabContentDiv.find('.container-div:eq('+tabHeadCount+') table:eq(0) tr:eq(0) td ')\n" +
            "            .each(function () {\n" +
            "                if($(this).text() == full_date)\n" +
            "                {\n" +
            "                    return false;\n" +
            "                }\n" +
            "                countTicketAvail++;\n" +
            "            });\n" +
            "        tabContentDiv.find('.container-div:eq('+tabHeadCount+') table:eq(0) tr:eq(1) td:eq('+ (countTicketAvail)+')')\n" +
            "            .find(\"a[id='\" + tabHeader + \"-\"+ (countTicketAvail - 1) +\"']\").each(function() {\n" +
            "            if($(this).text() == \"Book Now\") {\n" +
            "                $(this)[0].click();\n" +
            "            }\n" +
            "        });\n" +
            "    }\n" +
            "}\n" +
            "function getQuotaText(travelQuota) {\n" +
            "    var quota_txt = 'GN';\n" +
            "    if(travelQuota == 'GENERAL')\n" +
            "        quota_txt = 'GN';\n" +
            "    else if(travelQuota == 'PREMIUM TATKAL')\n" +
            "        quota_txt = 'PT';\n" +
            "    else if(travelQuota == 'PHYSICALLY HANDICAP')\n" +
            "        quota_txt = 'HP';\n" +
            "    else if(travelQuota == 'LADIES')\n" +
            "        quota_txt = 'LD';\n" +
            "    else if(travelQuota == 'TATKAL')\n" +
            "        quota_txt = 'CK';\n" +
            "    return quota_txt;\n" +
            "}\n" +
            "function IsLoadAvailabilityError() {\n" +
            "    if($(\"div[id='tabcontent']\").find('.container-div:eq(tabHeadCount)').find(\"#errorpanelid1\").length != 0) {\n" +
            "        console.log('Retirn on error');\n" +
            "        return true;\n" +
            "    }\n" +
            "    return false;\n" +
            "}\n" +
            "function isPaymentPage() {\n" +
            "    var elements = $(\"div[id='paymentMsgBox']\").get();\n" +
            "    if(elements != null && elements.length > 0) {\n" +
            "        return true;\n" +
            "    } else {\n" +
            "        elements = $(\"td[id='NETBANKING']\").get();\n" +
            "        if(elements != null && elements.length > 0) {\n" +
            "            return true;\n" +
            "        }\n" +
            "    }\n" +
            "    return false;\n" +
            "}\n" +
            "function isPassengerListPage() {\n" +
            "    var elements = $(\"table[id='addPassengerForm:psdetail']\").get();\n" +
            "    return !!(elements != null && elements.length > 0);\n" +
            "\n" +
            "}\n" +
            "function fillPassengerInformation() {\n" +
            "    $(\"#addPassengerForm\").find(\"td\").each(function() {\n" +
            "        if($(this).text().indexOf(\"Journey date :\") == 0) {\n" +
            "            var str0 = $(this).next().text().trim();\n" +
            "            var dd = ticketDetails['date-journey'].split(\"-\");\n" +
            "            dd[0] = ('0' + dd[0]).slice(-2);\n" +
            "            dd[1] = ('0' + dd[1]).slice(-2);\n" +
            "            var d = new Date(dd[2] + \"-\" + dd[1] + \"-\" + dd[0]);\n" +
            "            var m_names = [\"Jan\", \"Feb\", \"Mar\", \"Apr\", \"May\", \"Jun\", \"Jul\", \"Aug\", \"Sep\", \"Oct\", \"Nov\", \"Dec\"];\n" +
            "            var curr_date = ('0' + d.getDate()).slice(-2);\n" +
            "            var curr_month = d.getMonth();\n" +
            "            var curr_year = d.getFullYear();\n" +
            "            var str1 = curr_date + \"-\" + m_names[curr_month] + \"-\" + curr_year;\n" +
            "            if (str1 == str0) {\n" +
            "                var boarding_middle_station = ticketDetails[\"boarding\"];\n" +
            "                if(boarding_middle_station) {\n" +
            "                    var arrBoardingSplit = boarding_middle_station.split(\"-\");\n" +
            "                    if(arrBoardingSplit.length == 2) {\n" +
            "                        $(\"select[name='addPassengerForm:boardingStation']\").val(arrBoardingSplit[1].trim());\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                var noOfPassengers = 6;\n" +
            "                var noOfChilds = 2;\n" +
            "                if(ticketDetails['Quota'])\n" +
            "                {\n" +
            "                    if(ticketDetails['Quota'] == 'GENERAL') {\n" +
            "                        noOfPassengers = 6;\n" +
            "                        noOfChilds = 2;\n" +
            "                    }\n" +
            "                    else if(ticketDetails['Quota'] == 'TATKAL') {\n" +
            "                        noOfPassengers = 4;\n" +
            "                        noOfChilds = 2;\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                for(i=0; i < noOfPassengers; i++) {\n" +
            "                    console.log(\"fillPassengerInformation - pass details \" + i);\n" +
            "                    if (ticketDetails['passenger-info'][i]) {\n" +
            "                        var passDetails = ticketDetails['passenger-info'][i];\n" +
            "                        var rowElement = $('tr[id=\"addPassengerForm:psdetail:' + i + '\"]');\n" +
            "                        console.log(\"fillPassengerInformation - pass details \" + i + \" : adult.\");\n" +
            "                        $(rowElement).find(\"input.psgn-name\").val(passDetails.name);\n" +
            "                        $(rowElement).find(\"input.psgn-age\").val(passDetails.age);\n" +
            "                        $(rowElement).find(\"select.psgn-gender\").val(passDetails.gender);\n" +
            "                        $(rowElement).find(\"select.psgn-berth-choice\").val(passDetails.berth);\n" +
            "                        if (!(passDetails.nationality == 'indian')) {\n" +
            "                            $(\"select[name='addPassengerForm:psdetail:\" + i + \":idCardType']\").val(passDetails.idtype);\n" +
            "                            $(\"input[name='addPassengerForm:psdetail:\" + i + \":idCardNumber']\").val(passDetails.idno);\n" +
            "                        }\n" +
            "                        if (passDetails.food == 'Veg') {\n" +
            "                            $(\"select[name='addPassengerForm:psdetail:\" + i + \":foodChoice']\").val(\"V\");\n" +
            "                        } else {\n" +
            "                            $(\"select[name='addPassengerForm:psdetail:\" + i + \":foodChoice']\").val(\"N\");\n" +
            "                        }\n" +
            "                        if (passDetails.age >= 5 && passDetails.age <= 11) {\n" +
            "                            $(\"input[name='addPassengerForm:psdetail:\" + i + \":childBerthOpt']\").removeAttr(\"disabled\");\n" +
            "                        }\n" +
            "                        if (passDetails.senior == 'true') {\n" +
            "                            $(\"input[name='addPassengerForm:psdetail:\" + i + \":concessionOpt']\").prop('checked', true);\n" +
            "                        }\n" +
            "                    }\n" +
            "                    else {\n" +
            "                        $(\"input[name='addPassengerForm:psdetail:\"+j+\":psgnName']\").val(\"\");\n" +
            "                        $(\"input[name='addPassengerForm:psdetail:\"+j+\":psgnAge']\").val(\"\");\n" +
            "                        $(\"select[name='addPassengerForm:psdetail:\"+j+\":psgnGender']\").val(\" \");\n" +
            "                        $(\"select[name='addPassengerForm:psdetail:\"+j+\":berthChoice']\").val(\"  \");\n" +
            "                        $(\"select[name='addPassengerForm:psdetail:\"+j+\":idCardType']\").val(\"NULL_IDCARD\");\n" +
            "                        $(\"input[name='addPassengerForm:psdetail:\"+j+\":idCardNumber']\").val(\"\");\n" +
            "                        $(\"select[name='addPassengerForm:psdetail:\"+j+\":foodChoice']\").val(\" \");\n" +
            "                        $(\"input[name='addPassengerForm:psdetail:\"+j+\":concessionOpt']\").prop(\"disabled\", \"disabled\");\n" +
            "                        $(\"input[name='addPassengerForm:psdetail:\"+j+\":concessionOpt']\").prop('checked', false);\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                for(i=0; i < noOfChilds; i++){\n" +
            "                    if (ticketDetails['child-passenger-info'][i]) {\n" +
            "                        var passDetails = ticketDetails['child-passenger-info'][i];\n" +
            "                        console.log(\"fillPassengerInformation - pass details \" + i + \" : child. \");\n" +
            "                        $(\"input[name='addPassengerForm:childInfoTable:\"+ i +\":infantName']\").val(passDetails.name);\n" +
            "                        $(\"select[name='addPassengerForm:childInfoTable:\"+ i +\":infantAge']\").val(passDetails.age);\n" +
            "                        $(\"select[name='addPassengerForm:childInfoTable:\"+ i +\":infantGender']\").val(passDetails.gender);\n" +
            "                    }\n" +
            "                }\n" +
            "\n" +
            "                var mobNum = ticketDetails['mobile'];\n" +
            "                if(mobNum) {\n" +
            "                    $(\"input[id='addPassengerForm:mobileNo']\").val(mobNum);\n" +
            "                }\n" +
            "\n" +
            "                if (ticketDetails['Auto-Upgrade']=='true')\n" +
            "                    $(\"input[id='addPassengerForm:autoUpgrade']\").prop('checked', 'checked');\n" +
            "\n" +
            "                if (ticketDetails['book-confirm']=='false')\n" +
            "                    $(\"input[id='addPassengerForm:onlyConfirmBerths']\").prop('checked', 'checked');\n" +
            "                $(\"input[name='addPassengerForm:bookingCond'][value='\"+ticketDetails['book-id-cond']+\"']\").each(function() {\n" +
            "                    $(this)[0].click();\n" +
            "                });\n" +
            "\n" +
            "                if (ticketDetails['preferred-coach']=='false') {\n" +
            "                    $(\"input[id='addPassengerForm:prefCoachOpt']\").click();\n" +
            "                    $(\"input[id='addPassengerForm:coachID']\").val(ticketDetails['coachID']);\n" +
            "                }\n" +
            "\n" +
            "                if ($(\"input[id='#j_captcha']\").length > 0) {\n" +
            "                    $(\"input[id='#j_captcha']\").focus();\n" +
            "                } else {\n" +
            "                    if($(\"div[id='nlpCaptchaContainer']\").length > 0) {\n" +
            "                        $(\"div[id='nlpCaptchaContainer']\").css(\"width\", \"125%\");\n" +
            "                        if ($(\"#nlpCaptchaImg\").length > 0) {\n" +
            "                            $(\"input[id='#nlpAnswer']\").focus();\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            } else {\n" +
            "                $(\"input[id='addPassengerForm:replan']\").click();\n" +
            "                $.prompt(\"Ticket not available on selected date\");\n" +
            "            }\n" +
            "        }\n" +
            "    });\n" +
            "}\n" +
            "function fillPaymentDetails() {\n" +
            "    var alertVal = false;\n" +
            "    var availStatus = \"\";\n" +
            "    $(\"div[id*='jpBook:']\").find(\"td\").each(function(){\n" +
            "        var curchild = $(this).children(\"b\");\n" +
            "        if (curchild) {\n" +
            "            var curchild2 = curchild.find(\"label\");\n" +
            "            if(curchild2.length > 0){\n" +
            "                curchild2.each(function(){\n" +
            "                    if($(this).text().trim().indexOf(\"Availability :\") == 0) {\n" +
            "                        var str0 = $(this).parent().parent().next().text().trim().toLowerCase();\n" +
            "                        availStatus = str0;\n" +
            "                        if(str0.indexOf('available') < 0 && str0.indexOf('curr_avbl') < 0) {\n" +
            "                            alertVal = true;\n" +
            "                        }\n" +
            "                    }\n" +
            "                });\n" +
            "            }\n" +
            "        }\n" +
            "    });\n" +
            "\n" +
            "    $(\"td[id='\"+ticketDetails['payment-mode']+\"']\").click();\n" +
            "    if(ticketDetails['payment-mode'] == 'NETBANKING') {\n" +
            "        $(\"input[name='NETBANKING'][value='\"+ticketDetails['payment-mode-id']+\"']\").each(function(){\n" +
            "            $(this)[0].click();\n" +
            "        });\n" +
            "    }\n" +
            "    if(ticketDetails['payment-mode'] == 'CREDIT_CARD') {\n" +
            "        $(\"input[name='CREDIT_CARD'][value='\"+ticketDetails['payment-mode-id']+\"']\").each(function(){\n" +
            "            $(this)[0].click();\n" +
            "        });\n" +
            "    }\n" +
            "    if(ticketDetails['payment-mode'] == 'DEBIT_CARD') {\n" +
            "        $(\"input[name='DEBIT_CARD'][value='\"+ticketDetails['payment-mode-id']+\"']\").each(function(){\n" +
            "            $(this)[0].click();\n" +
            "        });\n" +
            "    }\n" +
            "    if(ticketDetails['payment-mode'] == 'CASH_CARD') {\n" +
            "        $(\"input[name='CASH_CARD'][value='\"+ticketDetails['payment-mode-id']+\"']\").each(function(){\n" +
            "            $(this)[0].click();\n" +
            "        });\n" +
            "    }\n" +
            "    if(ticketDetails['payment-mode'] == 'IRCTC_PREPAID') {\n" +
            "        $(\"input[name='IRCTC_PREPAID'][value='\"+ticketDetails['payment-mode-id']+\"']\").each(function(){\n" +
            "            $(this)[0].click();\n" +
            "        });\n" +
            "    }\n" +
            "\n" +
            "    if ((ticketDetails['payment-mode'] == \"CREDIT_CARD\" || ticketDetails['payment-mode'] == \"IRCTC_PREPAID\") &&\n" +
            "        (ticketDetails['payment-mode-id'] == 21 || ticketDetails['payment-mode-id'] == 59))\n" +
            "    {\n" +
            "        $(\"div[id='card_div_id']\").css(\"display\",\"\");\n" +
            "        $(\"select[id='card_type_id']\").val(ticketDetails['card-type']);\n" +
            "\n" +
            "        var ccNum = ticketDetails['card-no-value'];\n" +
            "        if(ccNum) {\n" +
            "            $(\"input[id='card_no_id']\").val(ccNum);\n" +
            "        }\n" +
            "\n" +
            "        $(\"select[id='card_expiry_mon_id']\").val(ticketDetails['expiry-mon']);\n" +
            "        $(\"input[id='card_expiry_year_id']\").val(ticketDetails['expiry-year']);\n" +
            "        $(\"input[id='card_name_id']\").val(ticketDetails['name-card']);\n" +
            "        if(ticketDetails['Card-CVV']) {\n" +
            "            $(\"input[id='cvv_no_id']\").val(ticketDetails['Card-CVV']);\n" +
            "            $(\"input[id='captcha_txt']\").focus();\n" +
            "        } else {\n" +
            "            $(\"input[id='cvv_no_id']\").focus();\n" +
            "        }\n" +
            "        if(alertVal){\n" +
            "            $.prompt('Ticket is in waiting list.');\n" +
            "        }\n" +
            "    }\n" +
            "    else\n" +
            "    {\n" +
            "        $(\"input[id='validate']\").click();\n" +
            "    }\n" +
            "\n" +
            "    $(\"input[name='j_captcha']\").keypress(function(e){\n" +
            "        var keycode = (e.keyCode ? e.keyCode : e.which);\n" +
            "        if(keycode == '13') {\n" +
            "            e.preventDefault();\n" +
            "            $(\"input[id='validate']\").click();\n" +
            "            return false;\n" +
            "        }\n" +
            "    });\n" +
            "}\n" +
            "\n" +
            "function doAutomation() {\n" +
            "    ticketDetails = JSON.parse(jsonString);\n" +
            "    if(!isUserLoggedIn())\n" +
            "    {\n" +
            "        console.log('debug user logged');\n" +
            "        if (isLoginPage()) {\n" +
            "            loginToForm();\n" +
            "            $(\"img[id='cimage']\").width('125%');\n" +
            "        } else {\n" +
            "            window.location.href = \"https://www.irctc.co.in/eticketing/loginHome.jsf\";\n" +
            "        }\n" +
            "    }\n" +
            "    else if(isTrainsListed()) {\n" +
            "        var quota = ticketDetails[\"Quota\"];\n" +
            "        var quotaRadioButtons = $(\"input[name='quota']\").get();\n" +
            "        var quota_txt = getQuotaText(quota);\n" +
            "        var eachRadioElement;\n" +
            "        for (var i = quotaRadioButtons.length - 1; i >= 0; i--) {\n" +
            "            eachRadioElement = quotaRadioButtons[i];\n" +
            "            if (eachRadioElement.value == quota_txt) {\n" +
            "                eachRadioElement.checked = true;\n" +
            "            }\n" +
            "        }\n" +
            "        var trainNo = ticketDetails[\"train-no\"].split(\":\")[0].trim();\n" +
            "        var berthclass = ticketDetails[\"class\"];\n" +
            "        var elements = $(\"table[id='avlAndFareForm:trainbtwnstns'] > tbody > tr[id*='avlAndFareForm:trainbtwnstns']\").get();\n" +
            "        if(elements != null && elements.length > 0) {\n" +
            "            var trainFound = false;\n" +
            "            var berthFound = false;\n" +
            "            $(\"table[id='avlAndFareForm:trainbtwnstns'] > tbody > tr[id*='avlAndFareForm:trainbtwnstns']\").each(function() {\n" +
            "                if($(this).find('td:first-child > a').text() == trainNo) {\n" +
            "                    trainFound = true;\n" +
            "                    $(this).find('td:last-child > a').each(function() {\n" +
            "                        if($(this).text() == berthclass) {\n" +
            "                            $(this)[0].click();\n" +
            "                            berthFound = true;\n" +
            "                            var dd = ticketDetails['date-journey'].split(\"-\");\n" +
            "                            dd[0] = ('0' + dd[0]).slice(-2);\n" +
            "                            dd[1] = ('0' + dd[1]).slice(-2);\n" +
            "                            var d = new Date(dd[2] + \"-\" + dd[1] + \"-\" + dd[0]);\n" +
            "                            var curr_date = d.getDate();\n" +
            "                            var curr_month = d.getMonth();\n" +
            "                            var curr_year = d.getFullYear();\n" +
            "                            var travelDate = curr_date + \"-\" + dd[1] + \"-\" + curr_year;\n" +
            "                            var fromStation = ticketDetails['boarding'].split('-');\n" +
            "                            var fromStationCode = fromStation[fromStation.length - 1].trim();\n" +
            "                            var toStation = ticketDetails['destination'].split('-');\n" +
            "                            var toStationCode = toStation[toStation.length - 1].trim();\n" +
            "                            timerID = setInterval(function () {\n" +
            "                                bookNow(trainNo, berthclass, quota);\n" +
            "                            }, 1000);\n" +
            "                        }\n" +
            "                    });\n" +
            "                }\n" +
            "            });\n" +
            "\n" +
            "            if (!(trainFound)) {\n" +
            "                $.prompt('The selected train is not available on the selected date. Please check if the data entered is correct.');\n" +
            "            } else if (!(berthFound)) {\n" +
            "                $.prompt('The selected berth \\'' + berthclass + '\\' is not available in the chosen train. Please check if the data entered is correct.');\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    else if(isPlanMyTravelAvailable()) {\n" +
            "        planMyTravel();\n" +
            "    }\n" +
            "    else if(isPaymentPage()) {\n" +
            "        fillPaymentDetails();\n" +
            "        $(\"img[id='cimage']\").width('125%');\n" +
            "    }\n" +
            "    else if(isPassengerListPage()) {\n" +
            "        fillPassengerInformation();\n" +
            "        $(\"img[id='bkg_captcha']\").width('125%');\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "doAutomation();\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irctc_main);

        final LxWebView webView = (LxWebView) findViewById(R.id.webView);

        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        //  http://stackoverflow.com/questions/21552912/android-web-view-inject-local-javascript-file-to-remote-webpage
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(getApplicationContext(), "Load Finsihsed", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 19) {
                    webView.requestFocus(View.FOCUS_DOWN);
                    view.evaluateJavascript(mJsForTicket, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Toast.makeText(getApplicationContext(), "on receive - eval script.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    webView.setInitialScale(1);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.showSoftInput(webView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    view.loadUrl(mJsForTicket);
                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl("http://www.irctc.co.in/");

    }
}
