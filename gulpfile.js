const { watch, src,dest,series,parallel} = require('gulp');
const sass = require('gulp-sass')(require('sass'));

function compilarBootstrap(){
    return src('./node_modules/bootstrap/scss/**/*.scss')
    .pipe(sass().on('error', sass.logError))
    //carpeta on es descarrega lo compilat
    .pipe(dest('css'));
}
function compilarSASS(){
    return src('src/main/resources/scss/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        //carpeta on es descarrega lo compilat
        .pipe(dest('src/main/resources/static'));
}
function autoCompila(){
    compilarSASS();
    watch('scss/**/*.scss', { usePolling:true } ,compilarSASS);
}
function copyjs(){
    return src('./node_modules/bootstrap/dist/js/*.js')
    //carpeta on es descarrega lo compilat
    .pipe(dest('js'));
}


//AQUEStES SI QUE FAN COSES;
exports.default = compilarSASS;
exports.watch = autoCompila;
exports.copyjs = copyjs;