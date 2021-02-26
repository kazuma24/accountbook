export default function Redirect(path){
    location.href = `http://localhost:8080/api/error/${path}`;
};