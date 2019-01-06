(function() {
	  var mockSearchResponse = [
	       {
	        "id":"5e6a7cef-a571-41e7-a063-a695281d86ca",
	        "type":"Full Time",
	        "url":"https://jobs.github.com/positions/5e6a7cef-a571-41e7-a063-a695281d86ca",
	        "created_at":"Wed Jan 02 14:44:05 UTC 2019",
	        "company":"Jun Group",
	        "company_url":"https://www.jungroup.com/",
	        "location":"New York, NY",
	        "title":"Senior Software Engineer",
	        "description":"The word JUN means truth, and our culture is about openness and honesty. Our mobile ad platform delivers millions of engagements across devices for clients like Intel, LG, Zynga, EA, and TechCrunch. These brands rely on us to reach their audience because everything we do is brand safe, viewable, and transparent."
	         }];
      
    /**
     * Initialize
     */
    function init() {
        // Register event listeners
        
//        $('recommend-btn').addEventListener('click', loadRecommendedItems);
        $('search-btn').addEventListener('click', searchJobs);
        $('nearby-btn').addEventListener('click', onReload);
//        loadRecommendedItems();
        
        var itemList = $('item-list');
        hideElement(itemList);
        
    }
    
    
    function onReload(){
    	var itemList = $('item-list');
        hideElement(itemList);
        
        var searchForm = $('search-form');
        showElement(searchForm);
    	
    }
    function searchJobs() {
        console.log('searchJobs');
        activeBtn('nearby-btn');

        // The request parameters
        var url = './search';
       
		var e = document.getElementById("cityLocation");
		var location = e.options[e.selectedIndex].text;
		
		var keyword = document.getElementById("keywords").value;
		
        var params = 'location=' + location + '&description=' + keyword;
        var req = JSON.stringify({});

        // display loading message
        showLoadingMessage('search jobs...');
        
        var searchForm = $('search-form');
        hideElement(searchForm);
        
        var itemList = $('item-list');
        showElement(itemList);

        // make AJAX call
        ajax('GET', url + '?' + params, req,
            // successful callback
            function(res) {
                var items = JSON.parse(res);
                if (!items || items.length === 0) {
                    showWarningMessage('No nearby jobs.');
                } else {
                    listItems(items);
                }
            },
            // failed callback
            function() {
                showErrorMessage('Cannot search jobs.');
            });
    }
    
    
    function loadRecommendedItems() {
    	activeBtn('recommend-btn');
    	var recommendedItems = mockSearchResponse.slice(1);
    	listItems(recommendedItems);
    }

    // -----------------------------------
    // Helper Functions
    // -----------------------------------

    /**
     * A helper function that makes a navigation button active
     * 
     * @param btnId -
     *            The id of the navigation button
     */
    function activeBtn(btnId) {
        var btns = document.getElementsByClassName('main-nav-btn');

        // deactivate all navigation buttons
        for (var i = 0; i < btns.length; i++) {
            btns[i].className = btns[i].className.replace(/\bactive\b/, '');
        }

        // active the one that has id = btnId
        var btn = $(btnId);
        btn.className += ' active';
    }

    function showLoadingMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> ' +
            msg + '</p>';
    }

    function showWarningMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> ' +
            msg + '</p>';
    }

    function showErrorMessage(msg) {
        var itemList = $('item-list');
        itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> ' +
            msg + '</p>';
    }

    	
    /**
     * AJAX helper
     * 
     * @param method -
     *            GET|POST|PUT|DELETE
     * @param url -
     *            API end point
     * @param callback -
     *            This the successful callback
     * @param errorHandler -
     *            This is the failed callback
     */
    function ajax(method, url, data, callback, errorHandler) {
        var xhr = new XMLHttpRequest();

        xhr.open(method, url, true);

        xhr.onload = function() {
			if (xhr.status === 200) {
				callback(xhr.responseText);
			} else if (xhr.status === 403) {
				onSessionInvalid();
			} else {
				errorHandler();
			}
        };

        xhr.onerror = function() {
            console.error("The request couldn't be completed.");
            errorHandler();
        };

        if (data === null) {
            xhr.send();
        } else {
            xhr.setRequestHeader("Content-Type",
                "application/json;charset=utf-8");
            xhr.send(data);
        }
    }
    /**
     * A helper function that creates a DOM element <tag options...>
     * 
     * @param tag
     * @param options
     * @returns
     */
    function $(tag, options) {
        if (!options) {
            return document.getElementById(tag);
        }

        var element = document.createElement(tag);

        for (var option in options) {
            if (options.hasOwnProperty(option)) {
                element[option] = options[option];
            }
        }

        return element;
    }

    function hideElement(element) {
        element.style.display = 'none';
    }

    function showElement(element, style) {
        var displayStyle = style ? style : 'block';
        element.style.display = displayStyle;
    }

    // -------------------------------------
    // Create item list
    // -------------------------------------

    /**
     * List items
     * 
     * @param items -
     *            An array of item JSON objects
     */
    function listItems(items) {
        // Clear the current results
        var itemList = $('item-list');
        itemList.innerHTML = '';

        for (var i = 0; i < items.length; i++) {
            addItem(itemList, items[i]);
        }
    }

    /**
     * Add item to the list
     * 
     * @param itemList -
     *            The
     *            <ul id="item-list">
     *            tag
     * @param item -
     *            The item data (JSON object)
     */
    function addItem(itemList, item) {
    	  var item_id = item.jobId;

          // create the <li> tag and specify the id and class attributes
          var li = $('li', {
              id: 'item-' + item_id,
              className: 'item'
          });

//          // set the data attribute
//          li.dataset.item_id = item.id;
          
          // section
          var section = $('div', {});

          // title
          var title = $('a', {
              href: item.githubLink,
              target: '_blank',
              className: 'title'
          });
          title.innerHTML = item.jobTitle;
          section.appendChild(title);

          var icons = $('div', {
              className: 'icons'
          });
          var building = $('i', {
              className: 'fa fa-building'
          });
          building.innerHTML = item.company;
          icons.appendChild(building);

          var map = $('i', {
              className: 'fa fa-map-marker'
          });
          map.innerHTML = item.location;
          icons.appendChild(map);

          section.appendChild(icons);

          var type = $('p', {
              className:'type'
          });
          type.innerHTML = item.jobType;
          section.appendChild(type);

          var descript = $('p',{
              className:'descript'
          });
          descript.innerHTML = item.jobDescription;
          section.appendChild(descript);

          li.appendChild(section);

          var section2 = $('div',{});
          var like = $('i',{
              className: 'fa fa-heart'
          });
          section2.appendChild(like);

          var dislikesection = $('div',{});
          var dislike = $('i',{
              className: 'fa fa-thumbs-down'
          });
          dislikesection.appendChild(dislike);

          section2.appendChild(dislikesection);

          li.appendChild(section2);
          itemList.appendChild(li);
    }
    
    init();
    
})();
