/**
 * This file builds the GUI for the DarkMMO world editor
 * @author jeff
 */


(function() {
    var _tileSetStore= new Ext.data.SimpleStore({
        data:[['foo'],['bar']],
        fields:['tileset']
    })
    var _tilesStore = new Ext.data.SimpleStore({
        data: [['foo'],['bar']],
        fields: ['tilename']
    });
    GUI = {
        setTileSets: function(array){
            array.sort();
            var arrayArray = []
            for (var i=0;i<array.length;i++){
                arrayArray.push([array[i]])
            }
            _tileSetStore.loadData(arrayArray)
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
                                displayField: 'tileset',
                                height: 100,
                                id: 'tileSetCombo'
                            }, {
                                region: 'center',
                                layout: 'accordion',
                                items: [{
                                        title: 'Features',
                                        items: [{
                                                xtype: 'grid',
                                                id:'featuresGrid',
                                                store: _tilesStore,
                                                autoHeight: true,
                                                autoWidth: true,
                                                columns: [{
                                                        header: 'Tile Name',
                                                        dataIndex: 'tilename'
                                                    }]
                                            }]
                                    }, {
                                        title: 'Groups',
                                        items: [{
                                                xtype: 'grid',
                                                id:'groupsGrid',
                                                store: _tilesStore,
                                                autoHeight: true,
                                                autoWidth: true,
                                                columns: [{
                                                        header: 'Tile Name',
                                                        dataIndex: 'tilename'
                                                    }]
                                            }]
                                    }, {
                                        title: 'Terrain',
                                        items: [{
                                                xtype: 'grid',
                                                id:'terrainGrid',
                                                store: _tilesStore,
                                                autoHeight: true,
                                                autoWidth: true,
                                                columns: [{
                                                        header: 'Tile Name',
                                                        dataIndex: 'tilename'
                                                    }]
                                            }]
                                    }]
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


