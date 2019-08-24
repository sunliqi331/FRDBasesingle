(function(){
  var lastdeletedID, lastdeletedTEXT, lastdeletedINDEX, count = 0;
  
  
  //generates a unique id
  function generateId(){
     return "reminder-" + +new Date();    
  }
  //saves an item to localStorage
  var saveReminder = function(id, content){
    localStorage.setItem(id, content);
  };
                                       
  var editReminder = function(id){
     var $this = $('#' + id);
     $this.focus()
          .append($('<i />', {
                        "class": "icon-save save-button", 
                         click: function(){
                                  
                                   $this.attr('contenteditable', 'false');

                                   var newcontent = $this.text(), saved = $('.save-notification');

                                   if(!newcontent) {
                                       var confirmation = swal('Delete this item?');
                                       if(confirmation) {
                                          removeReminder(id);
                                       }
                                   }
                                   else{
                                        localStorage.setItem(id, newcontent);
                                        saved.show();
                                        setTimeout(function(){
                                           saved.hide();
                                        },2000);
                                        $(this).remove();
                                        $('.icon-pencil').show();
                                   }
                
                                }
                 
           }));                
   };
  
   //removes item from localStorage
   var deleteReminder = function(id, content){
     localStorage.removeItem(id);
     count--;
//     updateCounter();
   };
 
   var UndoOption = function(){
      var undobutton = $('.undo-button');
      setTimeout(function(){
        undobutton.fadeIn(300).on('click', function(){
          createReminder(lastdeletedID, lastdeletedTEXT, lastdeletedINDEX);
          $(this).fadeOut(300);
        });
        setTimeout(function(){
          undobutton.fadeOut(1000);
        }, 3000);  
      },1000)
      
   };
 
   var removeReminder = function(id){
      var item = $('#' + id );
      lastdeletedID = id;
      lastdeletedTEXT = item.text();
      lastdeletedINDEX = item.index();
      
      item.addClass('removed-item')
          .one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e) {
              $(this).remove();
           });

      deleteReminder(id);
     //add undo option only if the edited item is not empty
      if(lastdeletedTEXT){
        UndoOption();
      }
    };
   
    var createReminder = function(id, content, index){
      var reminder = '<li id="' + id + '"><span class="list_todo">' + content + '</span></li>',
          list = $('.reminders li');
          
      
      if(!$('#'+ id).length){
        
        if(index && index < list.length){
          var i = index +1;
          reminder = $(reminder).addClass('restored-item');
          $('.reminders li:nth-child(' + i + ')').before(reminder);
        }
        if(index === 0){
          reminder = $(reminder).addClass('restored-item');
          $('.reminders').prepend(reminder);
        }
        if(index === list.length){
          reminder = $(reminder).addClass('restored-item');
          $('.reminders').append(reminder);
        }
        if(index === undefined){
          reminder = $(reminder).addClass('new-item todo-list-item');
          $('.reminders').append(reminder); 
        }

        var createdItem = $('#'+ id);

        createdItem.append($('<i />', {
                               "class" :"icon-trash delete-button",
                               "contenteditable" : "false",
                               click: function(){
                                  swal({ 
                                      title: "确认要删除？",  
                                      type: "warning", 
                                      showCancelButton: true, 
                                      closeOnConfirm: false, 
                                      confirmButtonText: "确认", 
                                      cancelButtonText: "取消",
                                      confirmButtonColor: "#ec6c62" 
                                  }, function() { 
                                      $.ajax({ 
                                          url: "do.php", 
                                          type: "DELETE" 
                                      }).done(function(data) { 
                                          swal("操作成功!", "已成功删除数据！", "success"); 
                                          removeReminder(id);
                                      }).error(function(data) { 
                                          swal("OMG", "删除操作失败了!", "error"); 
                                      }); 
                                  }); 
                              }
                  })); 

        createdItem.append($('<i />', {
                              "class" :"icon-pencil edit-button",
                              "contenteditable" : "false",
                              click: function(){
                                      createdItem.attr('contenteditable', 'true');
                                      editReminder(id);
                                      $(this).hide();
                              } 
                 }));
        createdItem.on('keydown', function(ev){
            if(ev.keyCode === 13) return false;
        });
        
        saveReminder(id, content);
      }
    };
//handler for input/* �������?�� �ز� www.zsucai.com */
    var handleInput = function(){
          $('#input-form').on('submit', function(event){
             var button = $('#text'),
              value = button.val();
              event.preventDefault();
              if (value){ 
                  var text = value;
                  var id = generateId();
                  createReminder(id, text);
                  button.val(''); 
              }
          });
     };
  
     var loadReminders = function(){
       if(localStorage.length!==0){
         for(var key in localStorage){
           var text = localStorage.getItem(key);
           if(key.indexOf('reminder') === 0){
             createReminder(key, text);
           }
         }
       }
     };
  //handler for the "delete all" button
     var handleDeleteButton = function(){
          $('.clear-all').on('click', function(){
            if(confirm('Are you sure you want to delete all the items in the list? There is no turning back after that.')){                 //remove items from DOM
              var items = $('li[id ^= reminder]');
              items.addClass('removed-item').one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e) {
                $(this).remove();
             });

              //look for items in localStorage /* �������?���� �� www.zsucai.com */that start with reminder- and remove them
              var keys = [];
              for(var key in localStorage){ 
                 if(key.indexOf('reminder') === 0){

                   localStorage.removeItem(key);
                 }
              }
              count = 0;
              updateCounter();
            }
          });
      };
  
    var init = function(){
           $('#text').focus();
           loadReminders();
           handleDeleteButton();
           handleInput();
    };
  //start all
  init();

})();


