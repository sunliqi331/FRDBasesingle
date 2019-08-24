var instance;
// the definition of source endpoints (the small blue ones)
var sourceEndpoint = {
		endpoint: "Dot",
		maxConnections: 3,
		
		//cssClass:"window",
		paintStyle: {
			strokeStyle: "#7AB02C",
			fillStyle: "transparent",
			radius: 7,
			lineWidth: 1,
		},
		isSource: true,
		isTarget: true,
		hoverPaintStyle: {fillStyle: "#7AB02C"},
		dragOptions: {},
		overlays: [
			[ "Label", {
				location: [0.5, 1.5],
				label: "Drag",
				cssClass: "endpointSourceLabel",
				visible:false
			} ]
		]
	},
	init = function (connection) {
	
		// connection.getOverlay("label").setLabel(connection.sourceId.substring(15) + "-" + connection.targetId.substring(15));
		

		var connectionInfo = storageManager.createConnectionInfo();
		connectionInfo.setSourceId(connection.sourceId);
		connectionInfo.setTargetId(connection.targetId);
		connectionInfo.setUuid(JSON.stringify(connection.getUuids()));
		var sourcePosition = connection.getUuids()[0];
		var targetPosition = connection.getUuids()[1];
		connectionInfo.setSourcePosition(sourcePosition);
		connectionInfo.setTargetPosition(targetPosition);
		
		// connectionInfo.setConnector(connection.getConnector().type);
		var graphicInfo = {};
		var typeArr = connection.getType();
		graphicInfo.graphicType = typeArr[typeArr.length - 1];
		graphicInfo.connectorType = connection.getConnector().type;
		graphicInfo.paintStyle = connection.getPaintStyle();
		connectionInfo.setLineType(JSON.stringify(graphicInfo));

		var connections = storageManager.generateConnectionInfoList(JSON.parse(localStorage.getItem("connections")));
		if(connections.length != 0){
			for(var i = 0; i < connections.length; i++){
				var connection = connections[i];
				if(connection.getSourcePosition() == connectionInfo.getSourcePosition() && connection.getTargetPosition() == connectionInfo.getTargetPosition()){
					connections.splice(i,1);
				}
			}
		}
		connections.push(connectionInfo);

		localStorage.setItem("connections",JSON.stringify(connections));
		
	};

	//定义锚点在线上对应的位置 
	
	var chorSite = function (){
		
		var x;
		var y;
		var xs = [];
		var ys = [];
		var dx = 1;
		var dy = 1; 
		var offsetX = 3;
		var offsetY = 3;
		for(x=0;x<=10;x++){
			xs.push(x/10);
			ys.push(x/10);
		}
		var positions1 = new Array();
		for(var j=0;j<ys.length;j++){
			var xSite =new Array();
				xSite.push(0);
				xSite.push(ys[j]);
				xSite.push(dx);
				xSite.push(dy);
				positions1.push(xSite);
		}
		var positions2 = new Array();
		for(var j=0;j<ys.length;j++){
			var xSite =new Array();
				xSite.push(1);
				xSite.push(ys[j]);
				xSite.push(dx);
				xSite.push(dy);
				positions2.push(xSite);
		}
		var positions3 = new Array();
		for(var j=0;j<xs.length;j++){
			var xSite =new Array();
				xSite.push(xs[j]);
				xSite.push(0);
				xSite.push(dx);
				xSite.push(dy);
				positions3.push(xSite);
		}
		var positions4 = new Array();
		for(var j=0;j<xs.length;j++){
			var xSite =new Array();
				xSite.push(xs[j]);
				xSite.push(1);
				xSite.push(dx);
				xSite.push(dy);
				positions4.push(xSite);
		}
		var totalPositions = [];
		totalPositions.push(positions1);
		totalPositions.push(positions2);
		totalPositions.push(positions3);
		totalPositions.push(positions4);
		
		return totalPositions;
	}
var _addEndpoints = function (toId, sourceAnchors, targetAnchors) {
	
	for (var i = 0; i < sourceAnchors.length; i++) {
		var sourceUUID = toId + sourceAnchors[i];
		instance.addEndpoint("flowchart" + toId, sourceEndpoint, {
			//anchor: sourceAnchors[i], uuid: sourceUUID
			//anchor: ["Top", "TopRight","TopLeft","Bottom", "BottomLeft","BottomRight", "LeftMiddle","Right"],
			anchor:chorSite(),uuid: sourceUUID 
			
		});
	}
	for (var j = 0; j < targetAnchors.length; j++) {
		var targetUUID = toId + targetAnchors[j];
		instance.addEndpoint("flowchart" + toId, sourceEndpoint, { 
			//anchor: targetAnchors[j], uuid: targetUUID 
			//anchor: ["Top", "TopRight","TopLeft","Bottom", "BottomLeft","BottomRight", "LeftMiddle","Right"],
		anchor:chorSite(),uuid: targetUUID 
		});
	}
	
};
jsPlumb.ready(function () {
	
	instance = jsPlumb.getInstance({
		// default drag options
		DragOptions: { cursor: 'pointer', zIndex: 2000 },
		// the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
		// case it returns the 'labelText' member that we set on each connection in the 'init' method below.
		ConnectionOverlays: [
			[ "Label", {
				location: 0.1,
				id: "label",
				cssClass: "aLabel",
				events:{
					tap:function() { alert("hey"); }
				}
			}]
		],
		PaintStyle: {
			lineWidth: 4,
			strokeStyle: "#000",
			joinstyle: "round",
			// outlineColor: "white",
			// outlineWidth: 2
		},
		HoverPaintStyle: {
			strokeStyle: "#f00",
		},
		Container: $('.demo')
	});

	var connectionStyles = {
		"arrow" : {
			overlays: [[ "Arrow", {
				location: 1,
				visible:true,
				id:"ARROW",
				events:{
					click:function() { alert("you clicked on the arrow overlay")}
				}
			}]]
		},
		"line" : {
			overlays: []
		}
	};
	instance.registerConnectionTypes(connectionStyles);

	// suspend drawing and initialise.
	instance.batch(function () {

		// listen for new connections; initialise them the same way we initialise the connections at startup.
		instance.bind("connection", function (connInfo, originalEvent) {
			init(connInfo.connection);
		});

		// listen for clicks on connections, and offer to delete connections on click.
		//
		instance.bind("click", function (conn, originalEvent) {
		   // if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
			 //   instance.detach(conn);
			 
			 instance.detach(conn);
			// conn.toggleType("basic");
		});

		instance.bind("connectionDrag", function (connection) {
			if (!$(connection.target).hasClass('window')) {
				// when the connect is created by the first time
				connection.setConnector(gSelectedGraphic.connectorType);
				connection.toggleType(gSelectedGraphic.graphicType);
				connection.setPaintStyle(gSelectedGraphic.paintStyle);
			}
			
			
		});

		instance.bind("connectionDragStop", function (connection) {
			
			
		});
		instance.bind("mouseover", function (endpoint, originalEvent) {
			// 
			if(endpoint.getParameter("isSource") == false){
				var element = endpoint.canvas;
				$(element).contextMenu(false);
			}
		});
		instance.bind("endpointClick", function (endpoint, originalEvent) {
			
			alert("******************元素的坐标left:"+endpoint[0]);
		});
		instance.bind("connectionMoved", function (params) {
			
		});
		instance.bind("connectionDetached", function (info, originalEvent) {
			var connect = info.connection;
			
			var connections = generateConnectionInfoList(JSON.parse(localStorage.getItem("connections")));

			if(connections.length != 0){
				for(var i = 0; i < connections.length; i++){
					var connection = connections[i];
					if(connection.getSourcePosition() == connect.getUuids()[0] && connection.getTargetPosition() == connect.getUuids()[1]){
						connections.splice(i,1);
					}

				}
			}
			localStorage.setItem("connections",JSON.stringify(connections));
		});
	});

	jsPlumb.fire("jsPlumbDemoLoaded", instance);
	storageManager.restoreData();
	initContainer();
	
	removeElm();
	
	//loadMonitorSelect();
});