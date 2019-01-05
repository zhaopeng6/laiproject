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
        
        $('recommend-btn').addEventListener('click', loadRecommendedItems);
        loadRecommendedItems();
    }
    
    function loadRecommendedItems() {
    	activeBtn('recommend-btn');
    	var recommendedItems = mockSearchResponse.slice(0);
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
        var item_id = item.id;

        // create the <li> tag and specify the id and class attributes
        var li = $('li', {
            id: 'item-' + item_id,
            className: 'item'
        });

        // set the data attribute
        li.dataset.item_id = item.id;
        // section
        var section = $('div', {});

        // title
        var title = $('a', {
            href: item.url,
            target: '_blank',
            className: 'title'
        });
        title.innerHTML = item.title;
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
        type.innerHTML = item.type;
        section.appendChild(type);

        var descript = $('p',{
            className:'descript'
        });
        descript.innerHTML = item.description;
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
