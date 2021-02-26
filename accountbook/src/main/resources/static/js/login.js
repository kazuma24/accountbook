{
    function check() {
        const loginId = document.getElementById('loginId').value.trim();
        const password = document.getElementById('password').value.trim();

        if(loginId === '') {
            alert('ログインIDを入力してください')
            return false;
        }
        if(password === '') {
            alert('パスワードを入力してください');
            return false;
        }

        //ログインID
        let loginIdReg = new RegExp(/^[a-zA-Z0-9]+$/);
        if(!(loginIdReg.test(loginId))) {
            alert('不正な値が入力されています');
            return false;
        }

        //パスワード
        let passwordReg = new RegExp(/^[a-zA-Z0-9]+$/);
        if(!(passwordReg.test(password))) {
            alert('不正な値が入力されています');
            return false;
        }
    }
}