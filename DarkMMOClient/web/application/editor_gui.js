/**
 * This file builds the GUI for the DarkMMO world editor
 * @author jeff
 */
function makeGui(){
    var _tilesStore = new Ext.data.SimpleStore({
        data: [['foo'],['bar']],
        fields: ['tilename']
    });
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
            split: true,
            items: [{
                region: 'north',
                xtype: 'combo',
                name: 'tileSetSelector',
                mode: 'local',
                store: [],
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

function setTileSets(array){
    Ext.get('tileSetCombo').store=array
}
