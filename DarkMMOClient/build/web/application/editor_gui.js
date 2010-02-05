/**
 * This file builds the GUI for the DarkMMO world editor
 * @author jeff
 */


(function() {
    var _tileSetStore= new Ext.data.ArrayStore({
        data:[['foo','Foo'],['bar','Bar']],
        fields:['name','displayName']
    })
    var _tileSetSelectionListener;
    var _tileSelectionListener;
    GUI = {
        setTileSets: function(array){
            array.sort(function(a,b){
                if (a[1]<b[1]){
                    return -1
                } else if(a[1]>b[1]){
                    return 1;
                }else {
                    return 0;
                }
            }); 
            _tileSetStore.loadData(array)
        },
        setTileSetSelectionListener: function(callback){
            _tileSetSelectionListener = callback
        },
        setTileSelectionListener: function(callback){
            console.log("Set tile selection listener: "+callback)
           _tileSelectionListener = callback
        },
        setTileAccordionData: function(dataObjectArray){
            var accordion = Ext.ComponentMgr.get('tileAccordion')
            console.log(accordion)
            accordion.removeAll(true)
            var callback = function(view,nodes){
                console.log(nodes)
                console.log(_tileSelectionListener)
                if (_tileSelectionListener){
                    _tileSelectionListener(view,nodes)
                }
            }
           
            for(var i=0;i<dataObjectArray.length;i++){
                var store =  new Ext.data.ArrayStore({
                    data:dataObjectArray[i].data,
                    fields:['tilenum','displayName','model']
                })
                var list =new Ext.list.ListView({
                    store: store,
                    multiSelect: false,
                    singleSelect:true,
                    emptyText: 'No images to display',
                    reserveScrollOffset: true,
                    columns: [{
                        dataIndex: 'displayName',
                        align: 'left',
                        width:1.0
                    }]
                })
                list.on('click',callback)
                var panel = new Ext.Panel({
                    title:dataObjectArray[i].groupName,
                    autoScroll: true
                })
                panel.add(list)
                list.select(0)
                accordion.add(panel)
            }
            accordion.doLayout()
        },
        makeGui: function(){
   
            var viewport = new Ext.Viewport({
                layout: 'border',
                renderTo: Ext.getBody(),
                items: [{
                    region: 'north',
                    xtype: 'toolbar',
                    items: [{
                        xtype: 'tbbutton',
                        text: 'File',
                        menu: [{
                            text: 'Load'
                        }, {
                            text: 'Save'
                        }]
                    }]
                }, {
                    region: 'south',
                    xtype: 'panel',
                    html: 'South'
                }, {
                    region: 'east',
                    layout: 'border',
                    title: 'Tile Sets',
                    width: 200,
                    items: [{
                        region: 'north',
                        xtype: 'combo',
                        name: 'tileSetSelector',
                        editable: false,
                        clearFilterOnReset: true,
                        triggerAction: 'all',
                        lastQuery: '', // == allQuery
                        mode: 'local',
                        store: _tileSetStore,
                        displayField: 'displayName',
                        height: 100,
                        id: 'tileSetCombo',
                        listeners:{
                            'select': function(combo,rec,idx){
                                if (_tileSetSelectionListener){
                                    _tileSetSelectionListener(combo,rec,idx)
                                }
                            }
                        }
                    }, {
                        region: 'center',
                        layout: 'accordion',
                        id: 'tileAccordion'
                       
                    }]
                }, {
                    region: 'west',
                    xtype: 'panel',
                    html: 'West'
                }, {
                    region: 'center',
                    xtype: 'panel',
                    html: 'Center'
                }]
            });
            console.log(viewport)
        }
    }
})()


