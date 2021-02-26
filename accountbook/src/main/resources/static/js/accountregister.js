{
    function cancelsubmit() {
        const name = document.getElementById('name').value.trim();
        const email = document.getElementById('email').value.trim();
        const loginId = document.getElementById('loginId').value.trim();
        const password = document.getElementById('password').value.trim();

        if(name === '' && email === '' && loginId === '' && password === '') {
            alert('情報を入力してください')
            return false;
        }

        //未入力チェック
        if(name === '') {
            alert('名前を入力してください');
            return false;
        }
        if(email === '') {
            alert('メールアドレスを入力してください');
            return false;
        }
        if(loginId === '') {
            alert('ログインIDを設定してください')
            return false;
        }
        if(password === '') {
            alert('パスワードを設定してください');
            return false;
        }

        //名前
        const nameLength = name.length;
        if(!(0 < nameLength && nameLength <= 30)) {
            alert('名前は1~30文字で設定してください');
            return false;
        }
        let nameReg = new RegExp(/[!"#$%&'()\*\+\-\.,\/:;<=>?@\[\\\]^_`{|}~]/g);
        if(nameReg.test(name)) {
            alert('名前に記号は含められません');
            return false;
        }

        //メールアドレス
        const emailLength = email.length;
        if(!(10 < emailLength && emailLength <= 50)) {
            alert('メールアドレスは10~50文字以内で設定してください');
            return false;
        }
        let emailReg = new RegExp(/^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]{1,}\.[A-Za-z0-9]{1,}$/);
        if(!(emailReg.test(email))) {
            alert('メールアドレスの形式で入力してください');
            return false;
        }

        //ログインID
        const loginIdLength = loginId.length;
        if(!(4 <= loginIdLength && loginIdLength <= 20)) {
            alert('ログインIDは4~20文字で設定してください')
            return false;
        }
        let loginIdReg = new RegExp(/^[a-zA-Z0-9]+$/);
        if(!(loginIdReg.test(loginId))) {
            alert('ログインIDは半角英数字のみで設定してください');
            return false;
        }

        //パスワード
        const passwordLength = password.length;
        if(!(8 <= passwordLength && passwordLength <= 20)) {
            alert('パスワードは8~20文字で設定してください')
            return false;
        }
        let passwordReg = new RegExp(/^[a-zA-Z0-9]+$/);
        if(!(passwordReg.test(password))) {
            alert('パスワードは半角英数字のみで設定してください');
            return false;
        }

        return true;
    }
}