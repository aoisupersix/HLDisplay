/**
 * ステータス情報の更新間隔[ms]
 */
var UPDATE_TIME = 2000

/**
 * 連続通信の防止用
 */
var jqxhr = null;

/**
 * ステータス情報のJSON
 */
var statusJson;

/**
 * 初期化処理を行います。
 * AndroidApp側から呼び出される関数です。
 * @param {string} jsonString ステータス情報のJSON
 */
function init(jsonString) {
  //ステータスの削除
  $("#memberStatus").empty();

  //ステータスの生成
  var json = JSON.parse(jsonString);
  var members = json["members"];
  var status = json["states"];
  statusJson = status;
  for(var i = 0; i < members.length; i++){
    var stateId = parseInt(members[i]["status"]);
    addCard(i, members[i]["name"], status[stateId]["name"], status[stateId]["color"]);
  }
  initStatusDetailButton(status);
}
/**
 * メンバー情報の更新を行います。
 * AndroidApp側から呼び出される関数です。
 * @param {string} jsonString ステータス情報のJSON
 */
function updateMemberStatus(jsonString) {
  var json = JSON.parse(jsonString);
  $('#memberStatus').children().each(function(index, element) {
    console.log("index-" + index);
    var card = $(element).find('.btn');
    var stateId = parseInt(json["status"]);
    if($(card).attr('data-id') == json["id"]) {
      //ステータスの更新
      var color = $(card).attr('data-color');
      $(card).attr({
        'data-id': json["id"],
        'data-name': json["name"],
        'data-statusText': statusJson[stateId]["name"],
        'data-color': statusJson[stateId]["color"]
      });
      $(element).removeClass('bg-' + color);
      $(element).addClass('bg-' + statusJson[stateId]["color"]);
      $(element).find('.card-header').text(json["name"]);
      $(element).find('.card-title').text(statusJson[stateId]["name"])
      return false;
    }
  })
}

/**
 * カード押下時にステータス詳細モーダルを表示します
 * @param {object} obj - クリックされたカード
 */
function showStatusDetail(obj) {
  $('#statusDetail-Name').text($(obj).attr('data-name'));
  $('#statusDetail-Status').text($(obj).attr('data-statusText'));
  $('#statusDetailModal').attr('data-id', $(obj).attr('data-id'));
  $('#statusDetailModal').modal();
}

/**
 * ステータス更新ボタン押下時にステータスを更新します
 * @param {object} obj - クリックされたステータス更新ボタン
 */
function statusChange(obj) {
  var userId = $('#statusDetailModal').attr('data-id');
  var stateId = $(obj).attr('data-id');
  pushStatus(userId, stateId);
  $('#statusDetailModal').modal('hide');
  android.updateState(userId, stateId);
}

/**
 * ステータス情報を適用させます
 * Jsonpのコールバック関数です
 * @param {JSON} json - サーバからのレスポンスデータ
 */
function updateLayout(json){
  //ステータスの削除
  $("#memberStatus").empty();

  //ステータスの生成
  var member = json["member"];
  var status = json["status"];
  for(var i = 0; i < member.length; i++){
    var stateId = parseInt(member[i].status);
    addCard(member[i].id, member[i].name, status[stateId].name, status[stateId].color);
  }
  initStatusDetailButton(status);
}

/**
 * Htmlにカードを追加します
 * @param {int} id - ユーザid
 * @param {string} name - ユーザ名
 * @param {string} statusText - ステータス状態を表す文字列
 * @param {string} color - ステータス状態に対応するBootStrapカラー
 */
function addCard(id, name, statusText, color){
  $('#memberStatus').append(
    $('<div class="card" style="margin: 5pt;width: 11rem;height: 9rem;"></div>').addClass('bg-' + color)
    .append($('<a href="#" class="btn btn-fix"></a>')
      .attr({
        'onClick': 'showStatusDetail(this)',
        'data-id': id,
        'data-name': name,
        'data-statusText': statusText,
        'data-color': color
      })
      .append($('<h4 class="card-header"></h4>').text(name))
      .append($('<div class="card-body"></div>')
        .append($('<h1 class="card-title" style="font-weight: bold;"></h1>').text(statusText))
      )
    )
  );
}

/**
 * ステータス更新用のボタンを作成します
 * @param {JSON} status - サーバからのレスポンスの内、ステータス情報の部分を抜き出したJSON
 */
function initStatusDetailButton(status){
  $('#statusButtons').empty();
  for(var i = 0; i < status.length; i++){
    $('#statusButtons').append(
      $('<Button class="btn btn-lg" style="width: 7em;height: 5em; margin: 3pt;"></Button>').addClass('btn-' + status[i].color)
      .text(status[i].name)
      .attr({
        'data-id': i,
        'onClick': 'statusChange(this)'
      })
    );
  }
}
