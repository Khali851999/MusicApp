package com.example.musicapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    public interface ItemClicked{
        void onItemClick(int index);
    }

    private Context mContext;
    private List<Album> albumList;
    ItemClicked activity;

    MediaPlayer mediaPlayer;


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        activity= (ItemClicked) mContext;
        this.albumList = albumList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

            mediaPlayer=new MediaPlayer();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
//                    activity.onItemClick(albumList.indexOf(view.getTag()));
                    try {

                        if (!mediaPlayer.isPlaying())
                        {
                            Toast.makeText(view.getContext(), "Playing  " + title.getText(),
                                    Toast.LENGTH_SHORT).show();
                            final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                            progressDialog.setTitle("Please Wait!!");
                            progressDialog.setMessage("Loading");
                            progressDialog.setButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(view.getContext(), "Stoping ", Toast.LENGTH_SHORT).show();
                                    mediaPlayer.stop();
                                    mediaPlayer.reset();
                                    mediaPlayer.release();


                                }
                            });
                            //protects from cancelling the dialog when touch outside
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            String url = PlayMusic(title.getText().toString());
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    progressDialog.dismiss();
                                    mediaPlayer.start();
                                    Log.i(TAG, "onClick: Playing Media");
                                }
                            });
                        }
                        else if(mediaPlayer.isPlaying())
                        {
                            Toast.makeText(view.getContext(), "Stoping  " + title.getText(),
                                    Toast.LENGTH_SHORT).show();
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                        }
                    }
                    catch (IOException e){e.printStackTrace();}
                }
            });
        }
    }

//returning the streaming url
    public String PlayMusic(String MusicName)
    {
        final String dataSource;

        switch (MusicName)
        {
            case "True Romance":
                dataSource="http://docs.google.com/uc?export=download&id=1NakIbWhVmgwuZ72AqTAtKi9ZSWyyO3B8";
                break;
            case "Xscape":
                dataSource="http://docs.google.com/uc?export=download&id=1_s86Fzcc5cKxCIZVh686C4ss7MtkqeM1";
                break;
            case "Maroon 5":
                dataSource="http://docs.google.com/uc?export=download&id=1F-D2gXr9LnCYiq3hfQuTHA95d7uKHq2N";
                break;
            //not original
            case "Born to Die":
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
            case "Honeymoon":
                dataSource="http://docs.google.com/uc?export=download&id=1pkILe8Z8fVPsKdqw4Q4kB44e2D52YnWr";
                break;
            case "I Need a Doctor":
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
            //not original
            case "Loud":
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
            //not original
            case "Legend":
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
            //not original
            case "Hello":
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
            case "Enrique Iglesias":
                dataSource="http://docs.google.com/uc?export=download&id=1Xi0VCuSSjWjEUjQaAIW20EqcK_SPKIqE";
                break;
            default:
                Toast.makeText(mContext, "Playing Default Music", Toast.LENGTH_SHORT).show();
                dataSource="http://docs.google.com/uc?export=download&id=1TfktBn8x_shhlrAzO2BoAPkp49LX2GK9";
                break;
        }
        return dataSource;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " songs");

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "Playing"+holder.title.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });


//to detect the index of clicked item
        holder.itemView.setTag(albumList.get(position));

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
