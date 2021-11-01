var spanElement = "<span>\n" +
    "            <svg id=\"icon_java\" t=\"1635747971599\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"12049\" width=\"200\" height=\"200\">\n" +
    "            <path class=\"mist\" d=\"M552.5 476.9c42.3 46.8-11.7 90.9-11.7 90.9s105.3-54 57.6-122.4c-45-63-80.1-94.5 108-204.3 0 0.9-295.2 75.6-153.9 235.8\"p-id=\"12050\"></path>\n" +
    "            <path class=\"mist\" d=\"M598.4 66.5s89.1 89.1-84.6 226.8C375.2 404 482.3 467 513.8 539.9 431.9 466.1 373.4 401.3 413 341 469.7 251.9 634.4 208.7 598.4 66.5\"p-id=\"12051\"></path>\n" +
    "            <path class=\"cofecup\" d=\"M695.6 728c161.1-83.7 86.4-165.6 35.1-153.9-13.5 2.7-18.9 4.5-18.9 4.5s4.5-7.2 14.4-9.9c103.5-36 182.7 106.2-33.3 163.8-0.9 0 1.8-2.7 2.7-4.5m-297 34.2s-33.3 18.9 23.4 26.1c67.5 7.2 103.5 7.2 178.2-7.2 0 0 20.7 13.5 46.8 23.4-168.3 71.1-380.7-4.5-248.4-42.3m-20.7-93.6s-37.8 27.9 20.7 33.3c72.9 7.2 131.4 9 229.5-11.7 0 0 14.4 14.4 36 21.6-203.4 60.3-431.1 4.5-286.2-43.2\" p-id=\"12052\"></path>\n" +
    "            <path class=\"cofecup\" d=\"M774.8 832.4s24.3 20.7-27.9 36c-98.1 30.6-405 39.6-490.5 1.8-30.6-13.5 27.9-31.5 45-36 18.9-4.5 30.6-2.7 30.6-2.7-34.2-25.2-221.4 45.9-94.5 66.6 342.9 54.9 626.4-25.2 537.3-65.7M413.9 570.5s-156.6 37.8-55.8 49.5c43.2 5.4 128.7 4.5 207-2.7 64.8-5.4 129.6-17.1 129.6-17.1s-23.4 9.9-39.6 21.6c-159.3 42.3-466.2 23.4-378-20.7 77.4-35.1 136.8-30.6 136.8-30.6\" p-id=\"12053\"></path>\n" +
    "            <path class=\"cofecup\" d=\"M430.1 954.8c154.8 9.9 392.4-5.4 398.7-79.2 0 0-11.7 27.9-128.7 49.5-132.3 24.3-296.1 21.6-392.4 5.4 1.8 0.9 21.6 18 122.4 24.3\" p-id=\"12054\"></path>\n" +
    "            </svg>\n" +
    "          </span>";
let array;
window.onload=function () {
    getCata0();
}

function getCata0() {
    $.ajax({
        url: "http://localhost:9264/lxy/cata",
        async: true,
        type: "POST",
        dataType: "json",
        success: function(result){
            if(result){
                array = result;
                const ans = result.filter((item, index)=> item.cat_level==1).sort((a,b)=>a.cat_sort-b.cat_sort);
                var string = "<ul>";
                for (var i=0;i<ans.length;i++){
                    string += `<li id="catli${ans[i].cat_id}" onclick="getCateOrRefresh(${ans[i].cat_id},'${ans[i].cat_link}')">${spanElement}${ans[i].cat_content}</li><div id="cat${ans[i].cat_id}" class="nextLevel"></div>`;
                    // string += `<li onclick="getCateOrRefresh(${ans[i].cat_id},'${ans[i].cat_link}')">${ans[i].cat_content}</li><div id="cat${ans[i].cat_id}" class="nextLevel"></div>`;
                }
                string+="</ul>";
                const d = document.querySelector("#catalogue");
                d.innerHTML = string;
            }
        }
    });
}

function handleId(id) {
    return array.filter((item, index) => item.cat_super_id == id).sort((a, b) => a.cat_sort - b.cat_sort);
}

function getCateOrRefresh(id,link) {
    // console.log("链接为:"+link);
    const ans = handleId(id);
    splice(ans,id,link);
    changeCofeColor(id,link);
}
function changeCofeColor(id,link) {
    if ($("#catli"+id).find("span").length>0){
        if ($("#cat" + id).css("display") == "block") {
            $("#catli" + id + " span svg path").eq(0).css("fill", "#FF0000");
            $("#catli" + id + " span svg path").eq(1).css("fill", "#FF0000");
            $("#catli" + id + " span svg path").eq(2).css("fill", "#6699FF");
            $("#catli" + id + " span svg path").eq(3).css("fill", "#6699FF");
            $("#catli" + id + " span svg path").eq(4).css("fill", "#6699FF");
        } else if ($("#cat" + id).css("display") == "none") {
            $("#catli" + id + " span svg path").css("fill", "#cccccc");
        }
    }
}

function insertCata() {

}

function splice(ans,id,link) {
    // console.log(link);
    if (link == "null"){
        if ($("#cat"+id).length>0&&$("#cat"+id).children().length>0){
            if ($("#cat"+id).css("display") == "none"){
                $("#cat"+id).css("display","block");
            }else if ($("#cat"+id).css("display") == "block"){
                $("#cat"+id).css("display","none");
            }
            return;
        }

        var string = "<ul>";
        var ml = ans[0].cat_level * 20;
        for (var i=0;i<ans.length;i++){
            if (ans[i].cat_children_number > 0){
                string += `<li id="catli${ans[i].cat_id}"  onclick="getCateOrRefresh(${ans[i].cat_id},'${ans[i].cat_link}')" style="padding-left: ${ml}px">${spanElement}${ans[i].cat_content}</li><div id="cat${ans[i].cat_id}" class="nextLevel"></div>`;
            }else{
                string += `<li id="catli${ans[i].cat_id}"  onclick="getCateOrRefresh(${ans[i].cat_id},'${ans[i].cat_link}')" style="padding-left: ${ml}px">${ans[i].cat_content}</li><div id="cat${ans[i].cat_id}" class="nextLevel"></div>`;
            }
        }
        string+="</ul>";
        $("#cat"+id).css("display","block");
        $("#cat"+id).append(string);
    }else{
        $.ajax({
            url: "http://localhost:9264/lxy/md",
            async: true,
            type: "POST",
            data: {
                link: link,
            },
            success: function(result){
                if(result){
                    document.getElementById('right').innerHTML = marked(result);
                    // console.log(result);
                }else{
                    console.log("没有请求到数据");
                }
            }
        });
    }
}

function grant(){
    var grantcode = $("#grant_input").val();
    $.ajax({
        url: "http://localhost:9264/lxy/grant/"+grantcode,
        async: true,
        type: "GET",
        dataType: "json",
        success: function(result){
            if(result == 200){
                $("#grant").css("display","none");
                $("#addCata").css("display","block");
            }
        }
    });
}