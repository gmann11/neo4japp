function del(a) {
  $.get("/del/" + a, function(data, status) {
    console.log("res", data);
    $("#results").empty();
    if (data.status == "failed") {
      var content='<li class="list-group-item">'+data.reason+'</li>';
      var list = $('<ul class="list-group"/>').html(content);
      $("#results").append(list);
    }
    else {
      var content='<li class="list-group-item">'+a+' deleted</li>';
      var list = $('<ul class="list-group"/>').html(content);
      $("#results").append(list);
    } 
  });
}

$(document).ready(function() {
  $("#add").click(function(){
    d=JSON.stringify({"empid":$("#eid").val(),"name":$("#name").val()});
    console.log("data", d);
    $.post({url: '/employee', type: 'POST', data: d, dataType: 'json', success: function(data, status){
      $("#addresults").empty();
      $("#results").empty();
      if (data.status == "failed") {
        var content='<li class="list-group-item">'+data.reason+'</li>';
        var list = $('<ul class="list-group"/>').html(content);
        $("#addresults").append(list);
      } 
      else {
        var content='<li class="list-group-item">'+$("#eid").val()+' added </li>';
        var list = $('<ul class="list-group"/>').html(content);
        $("#addresults").append(list);
      }
    }});
  });

  $("#search").click(function(){
    if ($('#searchterm').val()=='') {
      url = "/employees";
    }
    else {
      url = "/employee/" + $('#searchterm').val();
    }
    $.get(url, function(data, status){
      $("#results").empty();
      console.log("dd", data);
      if (data.status == "failed") {
        var content='<li class="list-group-item">'+data.reason+'</li>';
        var list = $('<ul class="list-group"/>').html(content);
        $("#results").append(list);
      } 
      else { 
        var items = data.results.map(function (item) {
          return '<a href="#" onclick="del(' + item.empid +'); return false;"><i class="fa fa-trash-o" style="font-size:24px"></i></a> ' + item.name + '   <span class="badge badge-pill badge-dark">' + item.empid + '</span>';
        });
        if (items.length) {
          var content = '<li class="list-group-item">' + items.join('</li><li class="list-group-item">') + '</li>';
          var list = $('<ul class="list-group"/>').html(content);
          $("#results").append(list);
        }
      }
    });
  });
});
