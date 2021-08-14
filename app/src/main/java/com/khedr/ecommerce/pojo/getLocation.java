//
//public class getLocation {
//
//
//boolean isGpsEnabled(){
//        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        try{
//        gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        }catch(Exception ex){
//        Log.d("medo",ex.getMessage());
//        }
//
//        try{
//        network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        }catch(Exception ex){
//        Log.d("medo",ex.getMessage());
//        }
//
//        if(!gps_enabled&&!network_enabled){
//// notify user
//        new AlertDialog.Builder(this)
//        .setMessage("gps network not enabled")
//        .setPositiveButton("open location settings",(paramDialogInterface,paramInt)->startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
//        .setNegativeButton("Cancel",null)
//        .show();
//        return false;
//        }else{
//        return true;
//        }
//        }
//
//@Override
//public void onClick(View v){
//        if(v==b.getLatLang){
//        if(isGpsEnabled()){
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//        setAddress();
//        Log.d("medo","setAddress");
//        }else{
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
//        }
//        }
//        }
//        }
//
//@SuppressLint("MissingPermission")
//void setAddress(){
//
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task->{
//        Location location=task.getResult();
//        if(location!=null){
//        Geocoder geocoder=new Geocoder(this,Locale.US);
//
//        try{
//        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//        String address=addresses.get(0).getAddressLine(0);
//        String cityName=addresses.get(0).getLocality();
//        String stateName=addresses.get(0).getAdminArea();
//        b.editText1.setText(address);
//        b.editText2.setText(cityName);
//        b.editText3.setText(stateName);
//
//        }catch(Exception e){
//        Log.d("medo","error "+e.getMessage());
//        e.printStackTrace();
//        }
//
//        }else{
//        Log.d("medo","null Location");
//        }
//
//        });
//
//        }
//}