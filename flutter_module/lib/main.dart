import 'package:flutter/material.dart';
import 'dart:ui';
import 'package:flutter/services.dart';
import 'dart:async';
void main() => runApp(MyApp(initParams: window.defaultRouteName,));

class MyApp extends StatelessWidget {
  final String initParams;
  MyApp({Key key,this.initParams}):super(key:key);
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in a Flutter IDE). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page',initParams: initParams,),
    );
  }
}

class MyHomePage extends StatefulWidget {
  final String initParams;
  MyHomePage({Key key, this.title,this.initParams}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  static const EventChannel _eventChannelPlugin =
  EventChannel('EventChannelPlugin');
  String showMessage = "";
  static const MethodChannel _methodChannelPlugin =
  const MethodChannel('MethodChannelPlugin');
  static const BasicMessageChannel<String> _basicMessageChannel =
  const BasicMessageChannel('BasicMessageChannelPlugin', StringCodec());
  bool _isMethodChannelPlugin = false;
  StreamSubscription _streamSubscription;
  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }
  @override
  void initState() {
    _streamSubscription = _eventChannelPlugin
        .receiveBroadcastStream('123')
        .listen(_onToDart, onError: _onToDartError);
    //使用BasicMessageChannel接受来自Native的消息，并向Native回复
    _basicMessageChannel
        .setMessageHandler((String message) => Future<String>(() {
      setState(() {
        showMessage = 'BasicMessageChannel:'+message;
      });
      return "收到Native的消息：" + message;
    }));
    super.initState();
  }
  @override
  void dispose() {
    if (_streamSubscription != null) {
      _streamSubscription.cancel();
      _streamSubscription = null;
    }
    super.dispose();
  }
  void _onToDart(message) {
    setState(() {
      showMessage = 'EventChannel:'+message;
    });
  }

  void _onToDartError(error) {
    print(error);
  }
  void _onTextChange(value) async {
    String response;
    try {
      if (_isMethodChannelPlugin) {
        //使用BasicMessageChannel向Native发送消息，并接受Native的回复
        response = await _methodChannelPlugin.invokeMethod('send', value);
      } else {
        response = await _basicMessageChannel.send(value);
      }
    } on PlatformException catch (e) {
      print(e);
    }
    setState(() {
      showMessage = response ?? "";
    });
  }
  void _onChanelChanged(bool value) =>
      setState(() => _isMethodChannelPlugin = value);
  @override
  Widget build(BuildContext context) {
    TextStyle textStyle = TextStyle(fontSize: 20);
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug painting" (press "p" in the console, choose the
          // "Toggle Debug Paint" action from the Flutter Inspector in Android
          // Studio, or the "Toggle Debug Paint" command in Visual Studio Code)
          // to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'HelloFlutter::::${widget.initParams}',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.display1,
            ),
            SwitchListTile(
              value: _isMethodChannelPlugin,
              onChanged: _onChanelChanged,
              title: Text(_isMethodChannelPlugin
                  ? "MethodChannelPlugin"
                  : "BasicMessageChannelPlugin"),
            ),
            TextField(
              onChanged: _onTextChange,
              decoration: InputDecoration(
                  focusedBorder: UnderlineInputBorder(
                      borderSide: BorderSide(color: Colors.white)),
                  enabledBorder: UnderlineInputBorder(
                      borderSide: BorderSide(color: Colors.white))),
            ),
            Text(
              'Native传来的数据：' + showMessage,
              style: textStyle,
            )
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
