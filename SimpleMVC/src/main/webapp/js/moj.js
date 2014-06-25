
$(document).ready(function () {
    $("#pastVisitsClient").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 1]],
        dateFormat: "ddmmyyyy",
        headers: {
            0: {sorter: "shortDate"}
        }
    }).tablesorterPager({container: $(".pager")});
    
    $("#upcomingVisitsClient").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 0]],
        dateFormat: "ddmmyyyy",
        headers: {
            0: {sorter: "shortDate"},
            4: {sorter: false}
        }
    }).tablesorterPager({container: $(".pager")});
    
    $("#visitsEmployee").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 0]],
        dateFormat: "ddmmyyyy",
        headers: {
            0: {sorter: "shortDate"},
            6: {sorter: false}
        }
    }).tablesorterPager({container: $(".pager")});
    
    $("#employees").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 0]],
        headers: {
            3: {sorter: false}
        }
    }).tablesorterPager({container: $(".pager")});
    
    $("#places").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 0]],
        headers: {
            4: {sorter: false}
        }
    }).tablesorterPager({container: $(".pager")});
    
    $("#selectPlace").tablesorter({
    	widgets: ["saveSort"],
        sortList: [[0, 0]],
        dateFormat: "ddmmyyyy",
        headers: {
            4: {sorter: false}
        }
    });
});


function sendPostToHref(elem, event) 
{
	event.preventDefault();

	var http = new XMLHttpRequest();
	var url = elem.href;

	http.onreadystatechange = function() 
	{
		if (http.readyState == 4) 
		{
			location.reload();
		}
	};

	http.open("POST", url, true);

	http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	http.setRequestHeader("Content-length", "0");
	http.setRequestHeader("Connection", "close");

	http.send(null);
}

function language(elem, event)
{
	sendPostToHref(elem, event);
}


function deleteVisit(elem, event, msg) 
{
	event.preventDefault();
	if(confirm(msg)) 
	{
		sendPostToHref(elem, event);
	}
}

function confirmVisit(elem, event) 
{
	sendPostToHref(elem, event);
}