package model.entity

/**
 * Created by Tyhj on 2017/6/12.
 */
class Update (var apkUrl:String,var info:String,var versionCode:String,var imageUrl:String,var version:Int){

    fun isCancelable():Boolean{
        return (version==0)
    }
}