package com.example.epsummonsim;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AuxInterface {
    /*@Override
    public void optionalSet(TypedArray arr, int str, ArrayAdapter<CharSequence> optional_adapter, TextView optional_text) {
        optional_adapter = ArrayAdapter.createFromResource(getApplicationContext(), arr, android.R.layout.simple_spinner_item); // set optional spinner to types of elemental summon
        optional_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optional_text.setText(str); // set optional text for elemental summon
    }*/

    @Override
    public int countRnd(String[] arr, String fit, int count, int number, int i) {
        int rnd = new Random().nextInt(number) + 1;
        for (i = 0; i < arr.length; i++) {
            if (arr[i].equals(fit))
                count++;
            if (count == rnd){
                break;
            }
        }
        return i;
    }

    @Override
    public int countRndTwo(String[] arr, String fit, int count, int number, int i, String[] arr2, String fit2) {
        int rnd = new Random().nextInt(number) + 1;
        for (i = 0; i < arr.length; i++) {
            if (arr[i].equals(fit) && arr2[i].equals(fit2))
                count++;
            if (count == rnd)
                break;
        }
        return i;
    }

    @Override
    public int countRndThree(String[] arr, String fit, int count, int number, int i, String[] arr2, String fit2, String[] arr3, String fit3) {
        int rnd = new Random().nextInt(number) + 1;
        for (i = 0; i < arr.length; i++) {
            if (arr[i].equals(fit) && arr2[i].equals(fit2) && arr3[i].equals(fit3))
                count++;
            if (count == rnd)
                break;
        }
        return i;
    }

    @Override
    public int summonElemental(String element, double rand, double bound1, double bound2, int number1, int number2, int number3, int i, int count, String[] _class, String[] _star, String[] _color) {
        if (rand <= bound1) // TC13 3*
        {
            i = countRndTwo(_class, "TC13", count, number1, i, _color, element);
        }
        else if (rand <= bound2) // TC20 4*
        {
            i = countRndThree(_class, "TC20", count, number2, i, _star, "4", _color, element);
        }
        else  // TC20 5*
        {
            i = countRndThree(_class, "TC20", count, number3, i, _star, "5", _color, element);
        }
        return i;
    }

    @Override
    public int summonSeasonalEvent(String element, double rand, double bound1, double bound2, double bound3, double bound4, double bound5, int number1, int number2, int number3, int number4, int number5, int number6, int i, int count, String[] _class, String[] _star) {
        if (rand <= bound1) // TC13     3*
        {
            i = countRnd(_class, "TC13", count, number1, i);
        }
        else if (rand <= bound2) // TC20     4*
        {
            i = countRndTwo(_class, "TC20", count, number2, i, _star, "4");
        }
        else if (rand <= bound3) // TC20     5*
        {
            i = countRndTwo(_class, "TC20", count, number3, i, _star, "5");
        }
        else if (rand <= bound4) // CH       3*
        {
            i = countRndTwo(_class, element, count, number4, i, _star, "3");
        }
        else if (rand <= bound5) // CH       4*
        {
            i = countRndTwo(_class, element, count, number5, i, _star, "4");
        } else  // CH       5*
        {
            i = countRndTwo(_class, element, count, number6, i, _star, "5");
        }
        return i;
    }

    @Override
    public String optType(String type) {
        if (type.equals("Christmas")) return "CH";
        else if(type.equals("Easter")) return "EH";
        else if(type.equals("Halloween")) return "HW";
        else if(type.equals("Summer Solstice")) return "SS";
        else if(type.equals("Guardians of Teltoc")) return "GT";
        else if(type.equals("Fables of Grimforest")) return "FG";
        else if(type.equals("Knights of Avalon")) return "KA";
        else if(type.equals("Pirates of Corellia")) return "PC";
        else return "RW";
    }

    @Override
    public int[] optNumber(String type, int hero_gt_three, int hero_gt_four, int hero_gt_five, int hero_fg_three, int hero_fg_four, int hero_fg_five, int hero_ka_three, int hero_ka_four, int hero_ka_five, int hero_pc_three, int hero_pc_four, int hero_pc_five, int hero_rw_three, int hero_rw_four, int hero_rw_five) {
        if(type.equals("Guardians of Teltoc")) return new int[]{hero_gt_three, hero_gt_four, hero_gt_five};
        else if(type.equals("Fables of Grimforest")) return new int[]{hero_fg_three, hero_fg_four, hero_fg_five};
        else if(type.equals("Knights of Avalon")) return new int[]{hero_ka_three, hero_ka_four, hero_ka_five};
        else if(type.equals("Pirates of Corellia")) return new int[]{hero_pc_three, hero_pc_four, hero_pc_five};
        else return new int[]{hero_rw_three, hero_rw_four, hero_rw_five};
    }


    private ProgressBar loading;
    private ObjectAnimator progressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String daily, epic_troop, epic_hero, elemental, valhalla, atlantis, seasonal, event, costume; // final string names of summons
        final String elemental_dark, elemental_fire, elemental_holy, elemental_ice, elemental_nature;       // final string names of elemental options
        final String seasonal_ch, seasonal_eh, seasonal_hw, seasonal_ss;                                    // final string names of seasonal options
        final String event_gt, event_fg, event_ka, event_pc, event_rw;                                      // final string names of event options
        final Spinner choose, optional;
        final TextView optional_text, name;   // optional spinner text
        final Button summon; // summon button
        final RatingBar stars;
        final ImageView img;
        final ProgressBar loading;

        choose = (Spinner) findViewById(R.id.choose);            // main spinner
        optional = (Spinner) findViewById(R.id.optionalChoose);  // optional spinner
        name = findViewById(R.id.name);                          // name of summoned hero/troop
        optional_text = findViewById(R.id.optionalInfo);         // optional text
        summon = findViewById(R.id.summon);                      // summon button
        stars = findViewById(R.id.stars);                        // number of summoned hero/troop stars
        img = findViewById(R.id.img);                            // image for hero/troop element
        loading = findViewById(R.id.loading);                    // loader when summon activated
        progressAnimator = ObjectAnimator.ofInt(loading, "progress", 0,100);

        img.setVisibility(View.GONE);
        stars.setVisibility(View.GONE);                          // invisible until summoned
        name.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choose_main, android.R.layout.simple_spinner_item); // adding options to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose.setAdapter(adapter);

        daily = getResources().getStringArray(R.array.choose_main)[0];       // summons
        epic_troop = getResources().getStringArray(R.array.choose_main)[1];
        epic_hero = getResources().getStringArray(R.array.choose_main)[2];
        elemental = getResources().getStringArray(R.array.choose_main)[3];
        valhalla = getResources().getStringArray(R.array.choose_main)[4];
        atlantis = getResources().getStringArray(R.array.choose_main)[5];
        seasonal = getResources().getStringArray(R.array.choose_main)[6];
        event = getResources().getStringArray(R.array.choose_main)[7];
        costume = getResources().getStringArray(R.array.choose_main)[8];

        elemental_dark = getResources().getStringArray(R.array.choose_elemental)[0];       // final string names of elemental options
        elemental_fire = getResources().getStringArray(R.array.choose_elemental)[1];
        elemental_holy = getResources().getStringArray(R.array.choose_elemental)[2];
        elemental_ice = getResources().getStringArray(R.array.choose_elemental)[3];
        elemental_nature = getResources().getStringArray(R.array.choose_elemental)[4];

        seasonal_ch = "CH";       // final string names of seasonal options
        seasonal_eh = "EH";
        seasonal_hw = "HW";
        seasonal_ss = "SS";

        event_gt = "GT";          // final string names of event options
        event_fg = "FG";
        event_ka = "KA";
        event_pc = "PC";
        event_rw = "RW";

        choose.setOnItemSelectedListener(new OnItemSelectedListener() // spinners listener
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                String selected_item = parent.getItemAtPosition(pos).toString();    // get selected item from spinner
                ArrayAdapter<CharSequence> optional_adapter = null; // declaration of adapter

                if(!selected_item.equals(daily) && !selected_item.equals(epic_troop) && !selected_item.equals(epic_hero) && !selected_item.equals(valhalla) && !selected_item.equals(atlantis) && !selected_item.equals(costume)) // if optional spinner required
                {
                    if(selected_item.equals(elemental)) // selected elemental summon as optional spinner
                    {
                        optional_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.choose_elemental, android.R.layout.simple_spinner_item); // set optional spinner to types of elemental summon
                        optional_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optional_text.setText(R.string.elemental_choose); // set optional text for elemental summon
                    }
                    else if(selected_item.equals(seasonal)) // selected seasonal summon as optional spinner
                    {
                        optional_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.choose_seasonal, android.R.layout.simple_spinner_item); // set optional spinner to types of seasonal summon
                        optional_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optional_text.setText(R.string.seasonal_choose); // set optional text for seasonal summon
                    }
                    else if(selected_item.equals(event)) // selected event summon as optional spinner
                    {
                        optional_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.choose_event, android.R.layout.simple_spinner_item); // set optional spinner to types of event summon
                        optional_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        optional_text.setText(R.string.event_choose); // set optional text for event summon
                    }
                    optional.setAdapter(optional_adapter);
                    optional.setVisibility(View.VISIBLE);   // make optional spinner visible
                    optional_text.setVisibility(View.VISIBLE); // make optional spinner text visible
                }
                else
                {
                    optional.setVisibility(View.INVISIBLE); // make optional spinner invisible
                    optional_text.setVisibility(View.INVISIBLE); // make optional spinner text invisible
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });



        summon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                stars.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                summon.setClickable(false);
                summon.setVisibility(View.GONE);

                if(true)
                {

                }
                String main = "";   // selected main summon
                String opt = "";    // selected optional summon
                String[] buff;      // contains one item from xml
                String[] troop_xml, troop, troop_star, troop_color;                     // troop info- xml data, troop name, troop quality, troop color
                int troop_ones = 0, troop_twos = 0, troop_threes = 0, troop_fours = 0; // number of troops with 1, 2, 3, 4 star
                String[] heroes_xml, heroes, heroes_star, heroes_color, heroes_class; // hero info- xml data, hero name, hero quality, hero color, hero class
                int hero_ones = 0, hero_twos = 0, hero_low_tc = 0, hero_high_tc_fours = 0, hero_high_tc_fives = 0; // number of heroes with 1, 2, 3*, 4*, 5* stars  *counted only if their class is tc13(low) or tc20(high)
                int hero_low_tc_dark = 0, hero_low_tc_fire = 0, hero_low_tc_holy = 0, hero_low_tc_ice = 0, hero_low_tc_nature = 0; // number of three star heroes with tc13 class based on their color
                int hero_high_tc_four_dark = 0, hero_high_tc_four_fire = 0, hero_high_tc_four_holy = 0, hero_high_tc_four_ice = 0, hero_high_tc_four_nature = 0; // number of four star heroes with tc20 class based on their color
                int hero_high_tc_five_dark = 0, hero_high_tc_five_fire = 0, hero_high_tc_five_holy = 0, hero_high_tc_five_ice = 0, hero_high_tc_five_nature = 0; // number of five star heroes with tc20 class based on their color
                int hero_vh_three = 0, hero_vh_four = 0, hero_vh_five = 0; // valhalla count
                int hero_ah_three = 0, hero_ah_four = 0, hero_ah_five = 0; // atlantis count
                int hero_ch_three = 0, hero_ch_four = 0, hero_ch_five = 0; // christmas count
                int hero_eh_three = 0, hero_eh_four = 0, hero_eh_five = 0; // easter count
                int hero_hw_three = 0, hero_hw_four = 0, hero_hw_five = 0; // halloween count
                int hero_ss_three = 0, hero_ss_four = 0, hero_ss_five = 0; // summer solstice count

                int hero_gt_three = 0, hero_gt_four = 0, hero_gt_five = 0; // guardian of teltoc count
                int hero_fg_three = 0, hero_fg_four = 0, hero_fg_five = 0; // fables of grimforest count
                int hero_ka_three = 0, hero_ka_four = 0, hero_ka_five = 0; // knights of avalon count
                int hero_pc_three = 0, hero_pc_four = 0, hero_pc_five = 0; // pirates of corellia count
                int hero_rw_three = 0, hero_rw_four = 0, hero_rw_five = 0; // riddles of wonderland count
                String[] costumes_xml, costumes, costumes_star, costumes_color; // costume info- xml data, costume name, costume quality, costume featured
                int cu_three = 0, cu_four = 0, cu_five = 0; // costume count
                int n; // length of xml file

                if (//choose != null &&
                        choose.getSelectedItem() != null) // if main spinner not null and selected item exists
                    main = choose.getSelectedItem().toString();
                else
                    main = choose.getItemAtPosition(0).toString(); // if doesn't
                if (optional != null && optional.getSelectedItem() != null) // if optional spinner not null and selected item exists
                    opt = optional.getSelectedItem().toString();

                troop_xml = getResources().getStringArray(R.array.troops); // troops gathering...
                n = troop_xml.length;   // number of troops
                troop = new String[n];
                troop_star = new String[n];
                troop_color = new String[n];

                for (int i = 0; i < n; i++) {
                    buff = troop_xml[i].split(" ");  // buffer takes item by item splitting string by whitespace
                    troop_star[i] = buff[buff.length - 1];  // takes troop star
                    troop_color[i] = buff[buff.length - 2]; // takes troop color
                    troop[i] = buff[0];
                    for (int j = 1; j < buff.length - 2; j++) // takes what's left from buffer as troop name
                        troop[i] += " " + buff[j];
                    if (troop_star[i].equals("1"))          // increment count of troop depending on how many stars it has
                        troop_ones++;
                    else if (troop_star[i].equals("2"))
                        troop_twos++;
                    else if (troop_star[i].equals("3"))
                        troop_threes++;
                    else
                        troop_fours++;
                }

                heroes_xml = getResources().getStringArray(R.array.heroes); // heroes gathering...
                n = heroes_xml.length;
                heroes = new String[n];
                heroes_star = new String[n];
                heroes_color = new String[n];
                heroes_class = new String[n];

                for (int i = 0; i < n; i++) {
                    buff = heroes_xml[i].split(" ");      // buffer takes item by item splitting string by whitespace
                    heroes_class[i] = buff[buff.length - 1];    // takes hero class
                    heroes_star[i] = buff[buff.length - 2];     // takes hero star
                    heroes_color[i] = buff[buff.length - 3];    // takes hero color

                    heroes[i] = buff[0];
                    for (int j = 1; j < buff.length - 3; j++) // takes what's left from buffer as hero name
                        heroes[i] += " " + buff[j];

                    if (heroes_star[i].equals("1")) // increment count of hero depending on how many stars it has
                        hero_ones++;
                    else if (heroes_star[i].equals("2"))
                        hero_twos++;
                    else if (heroes_star[i].equals("3")) {
                        if (heroes_class[i].equals("VH"))        // valhalla
                            hero_vh_three++;
                        else if (heroes_class[i].equals("AH"))   // atlantis
                            hero_ah_three++;
                        else if (heroes_class[i].equals(seasonal_ch))   // christmas
                            hero_ch_three++;
                        else if (heroes_class[i].equals(seasonal_eh))   // easter
                            hero_eh_three++;
                        else if (heroes_class[i].equals(seasonal_hw))   // halloween
                            hero_hw_three++;
                        else if (heroes_class[i].equals(seasonal_ss))   // summer solstice
                            hero_ss_three++;
                        else if (heroes_class[i].equals(event_gt))   // guardian of teltoc
                            hero_gt_three++;
                        else if (heroes_class[i].equals(event_fg))   // fables of grimforest
                            hero_fg_three++;
                        else if (heroes_class[i].equals(event_ka))   // knights of avalon
                            hero_ka_three++;
                        else if (heroes_class[i].equals(event_pc))   // pirates of corellia
                            hero_pc_three++;
                        else if (heroes_class[i].equals(event_rw))   // riddles of wonderland
                            hero_rw_three++;

                        if (heroes_class[i].equals("TC13")) {   // elemental
                            hero_low_tc++;
                            if (heroes_color[i].equals("Dark"))
                                hero_low_tc_dark++;
                            else if (heroes_color[i].equals("Fire"))
                                hero_low_tc_fire++;
                            else if (heroes_color[i].equals("Holy"))
                                hero_low_tc_holy++;
                            else if (heroes_color[i].equals("Ice"))
                                hero_low_tc_ice++;
                            else
                                hero_low_tc_nature++;
                        }
                    } else if (heroes_star[i].equals("4")) {
                        if (heroes_class[i].equals("VH"))        // valhalla
                            hero_vh_four++;
                        else if (heroes_class[i].equals("AH"))   // atlantis
                            hero_ah_four++;
                        else if (heroes_class[i].equals(seasonal_ch))   // christmas
                            hero_ch_four++;
                        else if (heroes_class[i].equals(seasonal_eh))   // easter
                            hero_eh_four++;
                        else if (heroes_class[i].equals(seasonal_hw))   // halloween
                            hero_hw_four++;
                        else if (heroes_class[i].equals(seasonal_ss))   // summer solstice
                            hero_ss_four++;
                        else if (heroes_class[i].equals(event_gt))   // guardian of teltoc
                            hero_gt_four++;
                        else if (heroes_class[i].equals(event_fg))   // fables of grimforest
                            hero_fg_four++;
                        else if (heroes_class[i].equals(event_ka))   // knights of avalon
                            hero_ka_four++;
                        else if (heroes_class[i].equals(event_pc))   // pirates of corellia
                            hero_pc_four++;
                        else if (heroes_class[i].equals(event_rw))   // riddles of wonderland
                            hero_rw_four++;

                        if (heroes_class[i].equals("TC20")) {   // elemental
                            hero_high_tc_fours++;
                            if (heroes_color[i].equals("Dark"))
                                hero_high_tc_four_dark++;
                            else if (heroes_color[i].equals("Fire"))
                                hero_high_tc_four_fire++;
                            else if (heroes_color[i].equals("Holy"))
                                hero_high_tc_four_holy++;
                            else if (heroes_color[i].equals("Ice"))
                                hero_high_tc_four_ice++;
                            else
                                hero_high_tc_four_nature++;
                        }
                    } else {
                        if (heroes_class[i].equals("VH"))        // valhalla
                            hero_vh_five++;
                        else if (heroes_class[i].equals("AH"))   // atlantis
                            hero_ah_five++;
                        else if (heroes_class[i].equals(seasonal_ch))   // christmas
                            hero_ch_five++;
                        else if (heroes_class[i].equals(seasonal_eh))   // easter
                            hero_eh_five++;
                        else if (heroes_class[i].equals(seasonal_hw))   // halloween
                            hero_hw_five++;
                        else if (heroes_class[i].equals(seasonal_ss))   // summer solstice
                            hero_ss_five++;
                        else if (heroes_class[i].equals(event_gt))   // guardian of teltoc
                            hero_gt_five++;
                        else if (heroes_class[i].equals(event_fg))   // fables of grimforest
                            hero_fg_five++;
                        else if (heroes_class[i].equals(event_ka))   // knights of avalon
                            hero_ka_five++;
                        else if (heroes_class[i].equals(event_pc))   // pirates of corellia
                            hero_pc_five++;
                        else if (heroes_class[i].equals(event_rw))   // riddles of wonderland
                            hero_rw_five++;

                        if (heroes_class[i].equals("TC20")) {   // elemental
                            hero_high_tc_fives++;
                            if (heroes_color[i].equals("Dark"))
                                hero_high_tc_five_dark++;
                            else if (heroes_color[i].equals("Fire"))
                                hero_high_tc_five_fire++;
                            else if (heroes_color[i].equals("Holy"))
                                hero_high_tc_five_holy++;
                            else if (heroes_color[i].equals("Ice"))
                                hero_high_tc_five_ice++;
                            else //if(heroes_color[i].equals("Nature"))
                                hero_high_tc_five_nature++;
                        }
                    }

                    //Log.d("marjan",  heroes_color[i] + " " + heroes_class[i] + " " + String.valueOf(hero_fg_three) + " "
                    //      + String.valueOf(hero_fg_four) + " " + String.valueOf(hero_fg_five) + heroes[i]);
                }

                costumes_xml = getResources().getStringArray(R.array.costumes); // costume gathering...
                n = costumes_xml.length;
                costumes = new String[n];
                costumes_star = new String[n];
                costumes_color = new String[n];
                costumes_color = new String[n];

                for (int i = 0; i < n; i++) {
                    buff = costumes_xml[i].split(" ");      // buffer takes item by item splitting string by whitespace
                    costumes_color[i] = buff[buff.length - 1]; // gets 1 if costume featured, 0 if not
                    costumes_star[i] = buff[buff.length - 2];     // costume stars
                    costumes[i] = buff[0];
                    for (int j = 1; j < buff.length - 2; j++)    // takes costume and costume hero name
                        costumes[i] += " " + buff[j];
                    if (costumes_star[i].equals("3"))       // count costume stars
                        cu_three++;
                    if (costumes_star[i].equals("4"))
                        cu_four++;
                    if (costumes_star[i].equals("5"))
                        cu_five++;
                }

                final StringBuilder summoned = new StringBuilder();   // string which is printed in output toast
                int i = 0;                                      // increment
                double rand = new Random().nextDouble();        // random number which determines what hero is going to be summoned
                int rnd;                                        // rnd picks one integer from interval[1, count(heroes that fit)]
                int ind = 0;                                    // if 0, hero is choosen. if 1, troop is choosen
                int count = 0;                                  // counting heroes that match to our fit. once it evens rnd, we have found our summon hero
                int ind_featured = 0;                           // if 0, costume is not featured. if 1, featured.
                if (main.equals(daily)) {
                    // DS 1*       32.0%        0.000 - 0.320
                    // DS 2*        31.3%       0.320 - 0.633
                    // TC13         3.3%        0.633 - 0.666
                    // troops 1*    16.0%       0.666 - 0.826
                    // troops 2*    15.7%       0.826 - 0.983
                    // troops 3*    1.7%        0.983 - 1.000
                    if (rand <= 0.32) // ds 1
                    {
                        i = countRnd(heroes_star, "1", count, hero_ones, i);
//                        rnd = new Random().nextInt(hero_ones) + 1;
////                        for (i = 0; i < heroes_star.length; i++) {
////                            if (heroes_star[i].equals("1")) // heroes that fit our search
////                                count++;
////                            if (count == rnd)       // when fitted heroes count is equal to rnd, our hero is found and we stop iterating
////                                break;
////                        }
                    } else if (rand <= 0.633) // ds 2
                    {
                        i = countRnd(heroes_star, "2", count, hero_twos, i);
                    } else if (rand <= 0.666) // ds 3
                    {
                        i = countRnd(heroes_class, "TC13", count, hero_low_tc, i);
                    } else if (rand <= 0.826) // troop 1
                    {
                        ind = 1;
                        i = countRnd(troop_star, "1", count, troop_ones, i);
                    } else if (rand <= 0.983) // troop 2
                    {
                        ind = 1;
                        i = countRnd(troop_star, "2", count, troop_twos, i);
                    } else // troop 3
                    {
                        ind = 1;
                        i = countRnd(troop_star, "3", count, troop_threes, i);
                    }
                } else if (main.equals(epic_troop)) {
                    // troops 3*    90.0%        0.000 - 0.900
                    // troops 4*    10.0%        0.900 - 1.000
                    ind = 1;
                    if (rand <= 0.9) {
                        i = countRnd(troop_star, "3", count, troop_threes, i);
                    } else {
                        i = countRnd(troop_star, "4", count, troop_fours, i);
                    }
                } else if (main.equals(epic_hero)) {
                    // TC13 3*        72.0%       0.000 - 0.720
                    // TC20 4*        26.5%       0.720 - 0.985
                    // TC20 5*         1.5%       0.985 - 1.000
                    if (rand <= 0.72) // TC13 3*
                    {
                        i = countRnd(heroes_class, "TC13", count, hero_low_tc, i);
                    } else if (rand <= 0.985) // TC20 4*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fours, i, heroes_star, "4");
                    } else  // TC20 5*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fives, i, heroes_star, "5");
                    }
                    rand = new Random().nextDouble();
                    if (rand <= 0.013) {
                        Log.d("logtxt", "Congratulations! You have summoned current Hero of the Month! :)");
                    }
                } else if (main.equals(elemental)) {
                    // TC13 3*        71.0%       0.000 - 0.710
                    // TC20 4*        26.5%       0.710 - 0.975
                    // TC20 5*         2.5%       0.975 - 1.000
                    if (opt.equals(elemental_dark))
                        i = summonElemental(elemental_dark, rand, 0.72, 0.985, hero_low_tc_dark, hero_high_tc_four_dark, hero_high_tc_five_dark, i, count, heroes_class, heroes_star, heroes_color);
                    else if (opt.equals(elemental_fire))
                        i = summonElemental(elemental_fire, rand, 0.72, 0.985, hero_low_tc_fire, hero_high_tc_four_fire, hero_high_tc_five_fire, i, count, heroes_class, heroes_star, heroes_color);
                    else if (opt.equals(elemental_holy))
                        i = summonElemental(elemental_holy, rand, 0.72, 0.985, hero_low_tc_holy, hero_high_tc_four_holy, hero_high_tc_five_holy, i, count, heroes_class, heroes_star, heroes_color);
                    else if (opt.equals(elemental_ice))
                        i = summonElemental(elemental_ice, rand, 0.72, 0.985, hero_low_tc_ice, hero_high_tc_four_ice, hero_high_tc_five_ice, i, count, heroes_class, heroes_star, heroes_color);
                    else if (opt.equals(elemental_nature))
                        i = summonElemental(elemental_nature, rand, 0.72, 0.985, hero_low_tc_nature, hero_high_tc_four_nature, hero_high_tc_five_nature, i, count, heroes_class, heroes_star, heroes_color);

                    double rand_bonus = new Random().nextDouble();
                    if (rand_bonus <= 0.013) {
                        Log.d("logtxt", "Congratulations! You have summoned current Hero of the Month! :)");
                    }
                } else if (main.equals(valhalla)) {
                    // TC13     3*        19.6%       0.000 - 0.196
                    // TC20     4*        11.9%       0.196 - 0.315
                    // TC20     5*         0.9%       0.315 - 0.324
                    // VH       3*        51.4%       0.324 - 0.838
                    // VH       4*        14.6%       0.838 - 0.984
                    // VH       5*         1.3%       0.984 - 0.997
                    // VH       Featured   0.3%       0.997 - 1.000
                    if (rand <= 0.196) // TC13     3*
                    {
                        i = countRnd(heroes_class, "TC13", count, hero_low_tc, i);
                    } else if (rand <= 0.315) // TC20     4*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fours, i, heroes_star, "4");
                    } else if (rand <= 0.324) // TC20     5*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fives, i, heroes_star, "5");
                    } else if (rand <= 0.838) // VH       3*
                    {
                        i = countRndTwo(heroes_class, "VH", count, hero_vh_three, i, heroes_star, "3");
                    } else if (rand <= 0.984) // VH       4*
                    {
                        i = countRndTwo(heroes_class, "VH", count, hero_vh_four, i, heroes_star, "4");
                    } else if (rand <= 0.997) // VH       5*
                    {
                        i = countRndTwo(heroes_class, "VH", count, hero_vh_five, i, heroes_star, "5");
                    } else   // VH       Featured
                    {
                        Log.d("logtxt", "Congratulations! You have summoned currently Featured Valhalla Hero! :)");
                    }
                    double rand_bonus = new Random().nextDouble();
                    if (rand_bonus <= 0.013) {
                        Log.d("logtxt", "Congratulations! You have summoned current Hero of the Month! :)");
                    }
                } else if (main.equals(atlantis)) {
                    // TC13     3*        21.8%       0.000 - 0.218
                    // TC20     4*        11.9%       0.218 - 0.337
                    // TC20     5*         1.2%       0.337 - 0.349
                    // VH       3*        49.2%       0.349 - 0.841
                    // VH       4*        14.6%       0.841 - 0.987
                    // VH       5*         1.3%       0.987 - 1.000
                    if (rand <= 0.218) // TC13     3*
                    {
                        i = countRnd(heroes_class, "TC13", count, hero_low_tc, i);
                    } else if (rand <= 0.337) // TC20     4*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fours, i, heroes_star, "4");
                    } else if (rand <= 0.349) // TC20     5*
                    {
                        i = countRndTwo(heroes_class, "TC20", count, hero_high_tc_fives, i, heroes_star, "5");
                    } else if (rand <= 0.841) // AH       3*
                    {
                        i = countRndTwo(heroes_class, "AH", count, hero_ah_three, i, heroes_star, "3");
                    } else if (rand <= 0.987) // AH       4*
                    {
                        i = countRndTwo(heroes_class, "AH", count, hero_ah_four, i, heroes_star, "4");
                    } else  // AH       5*
                    {
                        i = countRndTwo(heroes_class, "AH", count, hero_ah_five, i, heroes_star, "5");
                    }
                    double rand_bonus = new Random().nextDouble();
                    if (rand_bonus <= 0.013) {
                        Log.d("logtxt", "Congratulations! You have summoned current Hero of the Month! :)");
                        Toast.makeText(getApplicationContext(), "Congratulations! You have summoned current Hero of the Month! :)", Toast.LENGTH_SHORT).show();
                    }
                } else if (main.equals(seasonal)) {
                    if (opt.equals("Christmas")) {
                        // TC13     3*        64.0%       0.000 - 0.640
                        // TC20     4*        23.3%       0.640 - 0.873
                        // TC20     5*         0.9%       0.873 - 0.882
                        // CH       3*           8%       0.882 - 0.962
                        // CH       4*         3.2%       0.962 - 0.994
                        // CH       5*         0.6%       0.994 - 1.000
                        i = summonSeasonalEvent(seasonal_ch, rand, 0.640, 0.873, 0.882, 0.962, 0.987, hero_low_tc, hero_high_tc_fours, hero_high_tc_fives, hero_ch_three, hero_ch_four, hero_ch_five, i, count, heroes_class, heroes_star);
                    } else if (opt.equals("Easter")) {
                        // TC13     3*        56.8%       0.000 - 0.568
                        // TC20     4*        20.8%       0.568 - 0.776
                        // TC20     5*           1%       0.776 - 0.786
                        // EH       3*        14.2%       0.786 - 0.928
                        // EH       4*         5.7%       0.928 - 0.985
                        // EH       5*         1.5%       0.985 - 1.000
                        i = summonSeasonalEvent(seasonal_eh, rand, 0.568, 0.776, 0.786, 0.928, 0.985, hero_low_tc, hero_high_tc_fours, hero_high_tc_fives, hero_eh_three, hero_eh_four, hero_eh_five, i, count, heroes_class, heroes_star);
                    } else if (opt.equals("Halloween")) {
                        // TC13     3*        64.0%       0.000 - 0.640
                        // TC20     4*        23.3%       0.640 - 0.873
                        // TC20     5*         0.9%       0.873 - 0.882
                        // HW       3*           8%       0.882 - 0.962
                        // HW       4*         3.2%       0.962 - 0.994
                        // HW       5*         0.6%       0.994 - 1.000
                        i = summonSeasonalEvent(seasonal_hw, rand, 0.640, 0.873, 0.882, 0.962, 0.987, hero_low_tc, hero_high_tc_fours, hero_high_tc_fives, hero_hw_three, hero_hw_four, hero_hw_five, i, count, heroes_class, heroes_star);
                    } else if (opt.equals("Summer Solstice")) {
                        // TC13     3*        57.6%       0.000 - 0.576
                        // TC20     4*        20.8%       0.576 - 0.784
                        // TC20     5*         0.9%       0.784 - 0.793
                        // SS       3*        14.4%       0.793 - 0.937
                        // SS       4*         5.7%       0.937 - 0.994
                        // SS       5*         0.6%       0.994 - 1.000
                        i = summonSeasonalEvent(seasonal_ss, rand, 0.576, 0.784, 0.793, 0.937, 0.994, hero_low_tc, hero_high_tc_fours, hero_high_tc_fives, hero_ss_three, hero_ss_four, hero_ss_five, i, count, heroes_class, heroes_star);
                    }
                    double rand_bonus = new Random().nextDouble();
                    if (rand_bonus <= 0.013) {
                        Log.d("logtxt", "Congratulations! You have summoned current Hero of the Month! :)");
                        Toast.makeText(getApplicationContext(), "Congratulations! You have summoned current Hero of the Month! :)", Toast.LENGTH_SHORT).show();
                    }
                } else if (main.equals(event)) {
                    // TC13     3*        63.1%       0.000 - 0.631    //events share summoning probabilities
                    // TC20     4*        20.8%       0.631 - 0.839
                    // TC20     5*         1.5%       0.839 - 0.854
                    // GT       3*         7.9%       0.854 - 0.933
                    // GT       4*         5.7%       0.854 - 0.990
                    // GT       5*         1.0%       0.990 - 1.000
                    int[] event_numbers = optNumber(opt, hero_gt_three, hero_gt_four, hero_gt_five, hero_fg_three, hero_fg_four, hero_fg_five, hero_ka_three, hero_ka_four, hero_ka_five, hero_pc_three, hero_pc_four, hero_pc_five, hero_rw_three, hero_rw_four, hero_rw_five);
                    i = summonSeasonalEvent(optType(opt), rand, 0.631, 0.839, 0.854, 0.933, 0.990, hero_low_tc, hero_high_tc_fours, hero_high_tc_fives, event_numbers[0], event_numbers[1], event_numbers[2], i, count, heroes_class, heroes_star);
                } else if (main.equals(costume)) {
                    // CU     3*          71%       0.000 - 0.710
                    // CU     4*        26.5%       0.710 - 0.975
                    // CU     5*         1.2%       0.975 - 0.987
                    // CU     Featured   1.3%       0.987 - 1.000
                    ind = -1; // this way we make sure app don't print summon for hero/troop
                    if (rand <= 0.710) // CU     3*
                    {
                        i = countRnd(costumes_star, "3", count, cu_three, i);
                    } else if (rand <= 0.975) // CU     4*
                    {
                        i = countRnd(costumes_star, "4", count, cu_four, i);
                    } else if (rand <= 0.987) // CU     5*
                    {
                        i = countRnd(costumes_star, "5", count, cu_five, i);
                    } else // CU     Featured
                    {
                        ind_featured = 1;
                        Log.d("logtxt", "Congratulations! You have summoned featured costume! :)");
                        Toast.makeText(getApplicationContext(), "Congratulations! You have summoned featured costume! :)", Toast.LENGTH_SHORT).show();
                        /* kada otkrijes ko je featured
                        for(i = 0; i < costumes_star.length; i++)
                            if(costumes_color[i].equals("1"))
                                break;
                         */
                    }
                    if (ind_featured != 1) {
                        summoned.append(costumes[i]);
                        summoned.append("_");
                        summoned.append(costumes_color[i]);
                        summoned.append("_");
                        summoned.append(costumes_star[i]);
                        Log.d("logtxt", String.valueOf(summoned));
                    }
                }

                if (ind == 0) {
                    summoned.append(heroes[i]);
                    summoned.append("_");
                    summoned.append(heroes_color[i]);
                    summoned.append("_");
                    summoned.append(heroes_star[i]);
                    Log.d("logtxt", String.valueOf(summoned));
                } else if (ind == 1) {
                    summoned.append(troop[i]);
                    summoned.append("_");
                    summoned.append(troop_color[i]);
                    summoned.append("_");
                    summoned.append(troop_star[i]);
                    Log.d("logtxt", String.valueOf(summoned));
                }
                final String[] rez = summoned.toString().split("_");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int star;
                    star = Integer.parseInt(rez[2]);
                    name.setText(rez[0]);
                    stars.setRating(star);
                    View view = findViewById(R.id.stars).getRootView();

                    if(rez[1].equals("Dark"))
                    {
                        img.setBackgroundResource(R.drawable.element_dark);
                        // color = color - (number-1)*0x11111111;
                        view.setBackgroundColor(0xffc55fd9);
                    }
                    else if(rez[1].equals("Fire"))
                    {
                        img.setBackgroundResource(R.drawable.element_fire);
                        view.setBackgroundColor(0xffffa15e);

                    }
                    else if(rez[1].equals("Holy"))
                    {
                        img.setBackgroundResource(R.drawable.element_holy);
                        view.setBackgroundColor(0xfffafa7d);
                    }
                    else if(rez[1].equals("Ice"))
                    {
                        img.setBackgroundResource(R.drawable.element_ice);
                        view.setBackgroundColor(0xffbafdff);
                    }
                    else if(rez[1].equals("Nature"))
                    {
                        img.setBackgroundResource(R.drawable.element_nature);
                        view.setBackgroundColor(0xff8deb4b);
                    }
                    else
                    {
                        img.setBackgroundResource(R.drawable.upitnik);
                    }
                    loading.setVisibility(View.GONE);
                    stars.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    summon.setClickable(true);
                    summon.setVisibility(View.VISIBLE);
                }
            }, 1000);
            }
        });
    }
}
