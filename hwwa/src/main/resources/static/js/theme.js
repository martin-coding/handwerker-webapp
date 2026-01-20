(function () {
    const STORAGE_KEY = 'theme';
    const root = document.documentElement;

    function systemPrefersDark() {
        return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
    }

    function getTheme() {
        const stored = localStorage.getItem(STORAGE_KEY);
        if (stored === 'dark' || stored === 'light') return stored;
        return 'dark';
    }

    function applyTheme(theme) {
        root.setAttribute('data-bs-theme', theme);

        const btn = document.getElementById('themeToggleBtn');
        if (btn) btn.setAttribute('aria-pressed', theme === 'dark' ? 'true' : 'false');

        const icon = btn ? btn.querySelector('.material-symbols-outlined') : null;
        if (icon) icon.textContent = theme === 'dark' ? 'light_mode' : 'dark_mode';
    }

    function toggleTheme() {
        const current = root.getAttribute('data-bs-theme') || getTheme();
        const next = current === 'dark' ? 'light' : 'dark';
        localStorage.setItem(STORAGE_KEY, next);
        applyTheme(next);
    }

    applyTheme(getTheme());

    document.addEventListener('DOMContentLoaded', function () {
        applyTheme(getTheme());
    });

    document.addEventListener('click', function (e) {
        const btn = e.target && e.target.closest ? e.target.closest('#themeToggleBtn') : null;
        if (!btn) return;
        toggleTheme();
    });
})();