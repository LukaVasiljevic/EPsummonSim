package com.example.epsummonsim;

/*import android.content.res.TypedArray;
import android.widget.ArrayAdapter;
import android.widget.TextView;*/

import android.view.View;

public interface AuxInterface {

    //public void optionalSet(TypedArray arr, int str, ArrayAdapter<CharSequence> optional_adapter, TextView optional_text);
    public int countRnd(String[] arr, String fit, int count, int number, int i);
    public int countRndTwo(String[] arr, String fit, int count, int number, int i, String[] arr2, String fit2);
    public int countRndThree(String[] arr, String fit, int count, int number, int i, String[] arr2, String fit2, String[] arr3, String fit3);
    public int summonElemental(String element, double rand, double bound1, double bound2, int number1, int number2, int number3, int i, int count, String[] _class, String[] _star, String[] _color);
    public int summonSeasonalEvent(String element, double rand, double bound1, double bound2, double bound3, double bound4, double bound5, int number1, int number2, int number3, int number4, int number5, int number6, int i, int count, String[] _class, String[] _star);
    public String optType(String type);
    public int[] optNumber(String type, int hero_gt_three, int hero_gt_four, int hero_gt_five, int hero_fg_three, int hero_fg_four, int hero_fg_five, int hero_ka_three, int hero_ka_four, int hero_ka_five, int hero_pc_three, int hero_pc_four, int hero_pc_five, int hero_rw_three, int hero_rw_four, int hero_rw_five);
}

