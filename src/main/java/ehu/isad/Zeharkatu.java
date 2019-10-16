package ehu.isad;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Zeharkatu {

    public Zeharkatu() throws Exception {
        Kautotzea.getKautotzea().kautotu();
    }

    public HashMap<String,String> bildumakLortu() throws FlickrException, IOException {
        HashMap<String,String> bildumaIdIzena = new HashMap<String,String>(); // bildumen ID (gakoa) eta izenak gordeko dituen HashMap-a sortu
        PhotosetsInterface pi = Kautotzea.getKautotzea().getFlickr().getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        Iterator sets = pi.getList(Kautotzea.getKautotzea().getNsid()).getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu
        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // uneko bilduma lortu
            bildumaIdIzena.put(set.getTitle(),set.getId()); // uneko bildumaren ID eta izena HashMap-era gehitu
        }
        bildumaIdIzena.put("Bilduma barik","0");
        return bildumaIdIzena;
    }

    /*public ArrayList<Photo> bildumaArgazkiakLortu(String pBildumaID) throws FlickrException {
        //ArrayList<String> argazkiIzenak = new ArrayList<String>(); // bildumen izenak gordeko dituen ArrayList-a sortu
        PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        PhotoList photos = pi.getPhotos(pBildumaID, 500, 1);  // bildumaren lehenengo 500 argazki lortu
        //Collection currentSet = photos; // bildumaren argazkiak lortu
        return photos;
    }*/

    public ArrayList<Photo> bildumaArgazkiakLortu(String pBildumaID) throws FlickrException, IOException {
        HashMap<String,Photo> emaitza = new HashMap<String,Photo>();
        PhotosetsInterface pi = Kautotzea.getKautotzea().getFlickr().getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = Kautotzea.getKautotzea().getFlickr().getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        PhotoList photos = new PhotoList();
        if(pBildumaID != "0"){
            photos = pi.getPhotos(pBildumaID, 500, 1);  // bildumaren lehenengo 500 argazki lortu
        }
        else{
            int notInSetPage = 1;  // argazki batzuk bilduma batean sartu gabe egon daitezke...
            //Collection notInASet = new ArrayList(); // horiek ere jaso nahi ditugu
            while (true) { // lortu bildumarik gabeko argazkiak, 50naka
                Collection nis = photoInt.getNotInSet(50, notInSetPage);
                photos.addAll(nis);
                if (nis.size() < 50) {
                    break;
                }
                notInSetPage++;
            }
        }
        /*for(int i=0; i<photos.size(); i++){
            Photo unekoa = (Photo)photos.get(i);
            emaitza.put(unekoa.getId(),unekoa);
        }
        return emaitza;
        */
        return photos;
    }

    public static void main(String[] args) throws Exception {
        Kautotzea.getKautotzea().kautotu();
    }

}
