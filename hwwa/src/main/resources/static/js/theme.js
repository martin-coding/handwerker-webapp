(function () {
    if (window.__craftmanagerThemeInit) return;
    window.__craftmanagerThemeInit = true;

    const STORAGE_KEY = 'theme';
    const root = document.documentElement;

    function normalizeTheme(value) {
        return value === 'dark' || value === 'light' ? value : null;
    }

    function getTheme() {
        const stored = normalizeTheme(localStorage.getItem(STORAGE_KEY));
        if (stored) return stored;
        return 'light';
    }

    function applyTheme(theme) {
        const t = normalizeTheme(theme) || 'light';

        root.setAttribute('data-bs-theme', t);

        const btn = document.getElementById('themeToggleBtn');
        if (btn) btn.setAttribute('aria-pressed', t === 'dark' ? 'true' : 'false');

        const icon = btn ? btn.querySelector('.material-symbols-outlined') : null;
        if (icon) icon.textContent = t === 'dark' ? 'light_mode' : 'dark_mode';
    }

    function toggleTheme() {
        const current = normalizeTheme(root.getAttribute('data-bs-theme')) || getTheme();
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