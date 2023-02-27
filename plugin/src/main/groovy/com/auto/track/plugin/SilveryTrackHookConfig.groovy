package com.auto.track.plugin

import org.objectweb.asm.Opcodes

class SilveryTrackHookConfig {
    public final static HashMap<String, SilveryTrackMethodCell> LAMBDA_METHODS = new HashMap<>()
    static {
        SilveryTrackMethodCell onClick = new SilveryTrackMethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'Landroid/view/View$OnClickListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD])
        LAMBDA_METHODS.put(onClick.parent + onClick.name + onClick.desc, onClick)
        SilveryTrackMethodCell onCheckedChanged = new SilveryTrackMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'Landroid/widget/CompoundButton$OnCheckedChangeListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD])
        LAMBDA_METHODS.put(onCheckedChanged.parent + onCheckedChanged.name + onCheckedChanged.desc, onCheckedChanged)

        SilveryTrackMethodCell onRatingChanged = new SilveryTrackMethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'Landroid/widget/RatingBar$OnRatingBarChangeListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD])
        LAMBDA_METHODS.put(onRatingChanged.parent + onRatingChanged.name + onRatingChanged.desc, onRatingChanged)


        SilveryTrackMethodCell onStopTrackingTouch = new SilveryTrackMethodCell(
                'onStopTrackingTouch',
                '(Landroid/widget/SeekBar;)V',
                'Landroid/widget/SeekBar$OnSeekBarChangeListener;',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD])
        LAMBDA_METHODS.put(onStopTrackingTouch.parent + onStopTrackingTouch.name + onStopTrackingTouch.desc, onStopTrackingTouch)

        SilveryTrackMethodCell onClick1 = new SilveryTrackMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'Landroid/content/DialogInterface$OnClickListener;',
                'trackViewOnClick',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD])
        LAMBDA_METHODS.put(onClick1.parent + onClick1.name + onClick1.desc, onClick1)

        SilveryTrackMethodCell onItemClick = new SilveryTrackMethodCell(
                'onItemClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'Landroid/widget/AdapterView$OnItemClickListener;',
                'trackViewOnClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD])
        LAMBDA_METHODS.put(onItemClick.parent + onItemClick.name + onItemClick.desc, onItemClick)

        SilveryTrackMethodCell onGroupClick = new SilveryTrackMethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'Landroid/widget/ExpandableListView$OnGroupClickListener;',
                'trackExpandableListViewGroupOnClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD])
        LAMBDA_METHODS.put(onGroupClick.parent + onGroupClick.name + onGroupClick.desc, onGroupClick)

        SilveryTrackMethodCell onChildClick = new SilveryTrackMethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'Landroid/widget/ExpandableListView$OnChildClickListener;',
                'trackExpandableListViewChildOnClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
                1, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD])
        LAMBDA_METHODS.put(onChildClick.parent + onChildClick.name + onChildClick.desc, onChildClick)

        SilveryTrackMethodCell onTabChanged = new SilveryTrackMethodCell(
                'onTabChanged',
                '(Ljava/lang/String;)V',
                'Landroid/widget/TabHost$OnTabChangeListener;',
                'trackTabHost',
                '(Ljava/lang/String;)V',
                1, 1,
                [Opcodes.ALOAD])
        LAMBDA_METHODS.put(onTabChanged.parent + onTabChanged.name + onTabChanged.desc, onTabChanged)

        // Todo: 扩展
    }
}